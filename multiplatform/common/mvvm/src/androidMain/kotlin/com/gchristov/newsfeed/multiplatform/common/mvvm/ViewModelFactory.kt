package com.gchristov.newsfeed.multiplatform.common.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

class ViewModelFactory(private val viewModelBlock: () -> ViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(p0: Class<T>): T & Any {
        @Suppress("UNCHECKED_CAST")
        return viewModelBlock() as (T & Any)
    }
}

@Suppress("unused")
inline fun <reified T : ViewModel> ViewModelStoreOwner.getViewModel(noinline viewModelBlock: () -> T) =
    ViewModelProvider(this, ViewModelFactory {
        viewModelBlock()
    }).get(T::class.java)

@Suppress("unused")
inline fun <reified T : ViewModel> ViewModelStoreOwner.createViewModelFactory(noinline viewModelBlock: () -> T) =
    ViewModelFactory {
        viewModelBlock()
    }