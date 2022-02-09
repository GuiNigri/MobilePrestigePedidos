package com.guinigri.prestige.mobile.pedido.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import retrofit2.Response

open class BaseCallViewModel():ViewModel() {

    fun validarResponse(responseMessage: String, result: BaseViewModel, context: Context){

        if(result == null && responseMessage == "Unauthorized"){
            Toast.makeText(context, "Autenticação expirada, faça login novamente.", Toast.LENGTH_LONG).show()
            return
        }
    }
}