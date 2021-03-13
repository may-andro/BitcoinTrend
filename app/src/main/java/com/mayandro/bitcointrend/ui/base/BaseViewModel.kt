package com.mayandro.bitcointrend.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancelChildren

//This is base class for viewmodel where it keeps the basic setup and clearing of data
abstract class BaseViewModel<VI: ViewInteractor>: ViewModel() {

    var viewInteractor: VI? = null
        set

    override fun onCleared() {
        viewModelScope.coroutineContext.cancelChildren()
        viewInteractor =null
        super.onCleared()
    }
}