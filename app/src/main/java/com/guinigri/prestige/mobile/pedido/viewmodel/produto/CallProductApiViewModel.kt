package com.guinigri.prestige.mobile.pedido.viewmodel.produto

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.guinigri.prestige.mobile.pedido.api.RetroFitClient
import com.guinigri.prestige.mobile.pedido.settings.Token
import com.guinigri.prestige.mobile.pedido.viewmodel.BaseCallViewModel
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CallProductApiViewModel: BaseCallViewModel() {

    fun obterProdutoPeloCodigoBarras(codigoBarras: String, context: Context){

        var call = RetroFitClient.getApplicationMainApiService().obterProdutoPeloCodigoBarras(codigoBarras,Token.getToken());

        call.enqueue(
            object : Callback<ProductViewModel> {

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<ProductViewModel>,
                    response: Response<ProductViewModel>
                ) {
                    var result = response.body();

                    validarResponse(response.message(), result!!, context)


                }

                override fun onFailure(call: Call<ProductViewModel>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                }


            }
        )
    }
}