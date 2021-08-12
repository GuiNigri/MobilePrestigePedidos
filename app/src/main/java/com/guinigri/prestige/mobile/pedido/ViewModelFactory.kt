package com.guinigri.prestige.mobile.pedido

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.guinigri.prestige.mobile.pedido.viewmodel.CallLoginApiViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory:ViewModelProvider.Factory {
    override fun <T:ViewModel?> create(modelClass: Class<T>):T{
        if(modelClass.isAssignableFrom(CallLoginApiViewModel::class.java)){
            return CallLoginApiViewModel() as T;
        }
        throw IllegalArgumentException("Class ViewModel not found");
    }
}