package com.guinigri.prestige.mobile.pedido.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import retrofit2.Response

open class BaseCallViewModel():ViewModel() {

    val MensagemAutenticacaoExpirada = "Autenticação expirada, faça login novamente."
    val MensagemSistemaIndisponivel = "Ocorreu um erro no sistema, por favor tente novamente"

    fun validarResponse(responseMessage: String, result: BaseViewModel, context: Context){

        if(result == null && responseMessage == "Unauthorized"){
            Toast.makeText(context, MensagemAutenticacaoExpirada, Toast.LENGTH_LONG).show()
            return
        }
    }

    fun notificacaoFalha(context: Context){
        Toast.makeText(context, MensagemSistemaIndisponivel, Toast.LENGTH_LONG).show()
    }
}