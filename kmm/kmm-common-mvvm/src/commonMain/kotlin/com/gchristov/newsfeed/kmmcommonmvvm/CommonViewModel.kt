package com.gchristov.newsfeed.kmmcommonmvvm

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel

abstract class CommonViewModel<S : Any>(initialState: S) : ViewModel() {

    private val _state = MutableLiveData(initialState)

    val state: LiveData<S> get() = _state

    protected fun setState(reducer: S.() -> S) {
        val currentState = _state.value
        val newState = currentState.reducer()
        if (newState != currentState) {
            _state.value = newState
        }
    }
}