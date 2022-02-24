package com.guinigri.prestige.mobile.pedido.viewmodel.empresa

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.guinigri.prestige.mobile.pedido.api.RetroFitClient
import com.guinigri.prestige.mobile.pedido.settings.Token
import com.guinigri.prestige.mobile.pedido.viewmodel.BaseCallViewModel
import com.guinigri.prestige.mobile.pedido.viewmodel.produto.ProductViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CallEmpresaApiViewModel: BaseCallViewModel() {

    var empresa = MutableLiveData<EmpresaApiResponse>()

    val mensagemEmpresaNaoEncontrada = "Empresa não encontrada"

    fun obterEmpresaPeloCnpj(cnpj: String, context: Context){

        var call = RetroFitClient.getApplicationMainApiService().obterEmpresaPeloCnpj(cnpj,
            Token.getToken());

        call.enqueue(
            object : Callback<EmpresaApiResponse> {

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<EmpresaApiResponse>,
                    response: Response<EmpresaApiResponse>
                ) {
                    var result = response.body();

                    if(response.code() == 422) {
                        Toast.makeText(context, mensagemEmpresaNaoEncontrada, Toast.LENGTH_LONG).show()
                        return
                    }

                    validarResponse(response.message(), result!!, context)

                    empresa.value = result;
                }

                override fun onFailure(call: Call<EmpresaApiResponse>, t: Throwable) {
                    notificacaoFalha(context);
                }


            }
        )
    }
}