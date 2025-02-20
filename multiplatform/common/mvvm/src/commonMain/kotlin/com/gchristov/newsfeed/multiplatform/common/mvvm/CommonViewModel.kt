package com.gchristov.newsfeed.multiplatform.common.mvvm

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.setValue
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
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
            // postValue vs setValue
            // see: https://stackoverflow.com/questions/51299641/difference-of-setvalue-postvalue-in-mutablelivedata/51299672#51299672
            _state.setValue(newState, async = false)
        }
    }

    /**
     * Executes [block] in a new coroutine running in the [dispatcher] context. The calling
     * thread is not blocked and all [suspend] operations within [block] will suspend it until they
     * complete, after which execution of [block] will continue synchronously.
     */
    protected fun launchUiCoroutine(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(dispatcher) {
            block()
        }
}