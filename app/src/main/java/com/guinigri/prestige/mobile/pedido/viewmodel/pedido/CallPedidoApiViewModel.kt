package com.guinigri.prestige.mobile.pedido.viewmodel.pedido

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.guinigri.prestige.mobile.pedido.api.RetroFitClient
import com.guinigri.prestige.mobile.pedido.api.request.pedido.RegistrarPedidoRequest
import com.guinigri.prestige.mobile.pedido.settings.Token
import com.guinigri.prestige.mobile.pedido.viewmodel.BaseCallViewModel
import com.guinigri.prestige.mobile.pedido.viewmodel.produto.ProductViewModel
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CallPedidoApiViewModel: BaseCallViewModel() {

    var numeroPedido = MutableLiveData<RegistrarPedidoResponse>()

    fun registrarPedido(request: RegistrarPedidoRequest, context: Context){

        val json = Gson().toJson(request)
        var body: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        val mensagemProblemaRegistrarPedido = "Erro ao registrar pedido, tente novamente, ou contate o suporte tecnico"

        var call = RetroFitClient.getApplicationMainApiService().registrarPedido(body,
            Token.getToken());

        call.enqueue(
            object : Callback<RegistrarPedidoResponse> {

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<RegistrarPedidoResponse>,
                    response: Response<RegistrarPedidoResponse>
                ) {
                    var result = response.body();

                    if(response.code() == 422) {
                        Toast.makeText(context, mensagemProblemaRegistrarPedido, Toast.LENGTH_LONG).show()
                        return
                    }

                    validarResponse(response.message(), result!!, context)

                    numeroPedido.value = result;
                }

                override fun onFailure(call: Call<RegistrarPedidoResponse>, t: Throwable) {
                    notificacaoFalha(context);
                }


            }
        )
    }
}