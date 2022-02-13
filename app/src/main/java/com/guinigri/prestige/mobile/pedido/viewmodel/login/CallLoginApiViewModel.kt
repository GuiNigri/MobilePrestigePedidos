package com.guinigri.prestige.mobile.pedido.viewmodel.login

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guinigri.prestige.mobile.pedido.api.RetroFitClient
import com.guinigri.prestige.mobile.pedido.settings.Token
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CallLoginApiViewModel:ViewModel() {

    var status = MutableLiveData<Boolean>();

    val mensagemSistemaIndisponivel = "Ocorreu um erro no sistema, por favor tente novamente"
    val mensagemUsuarioSenhaIncorreto = "Usuario ou senha incorretos"

    fun logar(email:String, password:String, context: Context){

        var json =  "{\"email\": \"$email\", \"password\": \"$password\"}";
        var body:RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);

        var call = RetroFitClient.getApplicationMainApiService().login(body);

        call.enqueue(
            object : Callback<LoginApiResponseViewModel>{

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<LoginApiResponseViewModel>,
                    response: Response<LoginApiResponseViewModel>
                ) {
                    var result = response.body();

                    if(result == null && response.message() == "Unauthorized"){
                        exibirNotificacao(mensagemUsuarioSenhaIncorreto, context)
                        status.value = false;

                        return;
                    }

                    Token.atualizarDados(result?.token!!, result?.expires!!, context);
                    status.value = true;
                }

                override fun onFailure(call: Call<LoginApiResponseViewModel>, t: Throwable) {
                    exibirNotificacao(mensagemSistemaIndisponivel, context)
                    status.value = false;
                }


            }
        )
    }

    private fun exibirNotificacao(mensagem: String, context: Context){
        Toast.makeText(context, mensagem, Toast.LENGTH_LONG).show()
    }
}