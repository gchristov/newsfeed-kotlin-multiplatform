package com.gchristov.newsfeed.kmmcommonmvvm

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.postValue
import dev.icerock.moko.mvvm.livedata.setValue
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @param dispatcher Used in [launchUiCoroutine] to launch new UI coroutines. Such coroutines will
 * be launched on the [dispatcher] context in order to run sequential blocking operations. Most
 * common value for this should be [Dispatchers.Main], and during tests [UnconfinedTestispatcher].
 * @param bgDispatcher Used in [launchBgCoroutine] to launch new coroutines that run on a background
 * thread using [bgDispatcher] context in order to run sequential blocking operations. Most
 * common value for this should be [Dispatchers.Default], and during tests [UnconfinedTestispatcher].
 */
abstract class CommonViewModel<S : Any>(
    // No default values provided to reinforce them to be correctly set during tests.
    private val dispatcher: CoroutineDispatcher,
    initialState: S
) : ViewModel() {
    private val _state = MutableLiveData(initialState)

    val state: LiveData<S> get() = _state

    /**
     * Update the state of the view-model. Must be called from the main (UI) thread.
     */
    protected fun setState(reducer: S.() -> S) {
        val currentState = _state.value
        val newState = currentState.reducer()
        if (newState != currentState) {
            // Fix for loading not showing on time
            // what it happens is there's a number of
            // calls enqueued here, and there's a 'missing update'
            // call 1 puts loading to true
            // call 2 puts it to false
            // but call 2 sees current state with loading false
            // as it's picking from _state.value
            // the queue is not refreshed quickly enough in this branch
            // The potential fix is always storing locally the 'actual'
            // currentState as the newState, the next tick will post the queue
            // but this may happen slower in the branch as it's in a coroutine?
            _state.setValue(newState, false)
        }
    }

    /**
     * Executes [block] in a new coroutine running in the [dispatcher] context. The calling
     * thread is not blocked and all [suspend] operations within [block] will suspend it until they
     * complete, after which execution of [block] will continue synchronously.
     */
    protected fun launchUiCoroutine(block: suspend () -> Unit) =
        viewModelScope.launch(dispatcher) {
            block()
        }
}