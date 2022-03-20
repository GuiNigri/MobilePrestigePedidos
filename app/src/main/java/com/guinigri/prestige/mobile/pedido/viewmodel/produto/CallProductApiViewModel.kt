package com.guinigri.prestige.mobile.pedido.viewmodel.produto

import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guinigri.prestige.mobile.pedido.api.RetroFitClient
import com.guinigri.prestige.mobile.pedido.settings.Token
import com.guinigri.prestige.mobile.pedido.viewmodel.BaseCallViewModel
import kotlinx.android.parcel.Parcelize
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class CallProductApiViewModel: BaseCallViewModel() {

    var produto = MutableLiveData<ProductViewModel>()

    val mensagemProdutoNaoEncontrado = "Produto não encontrado"
    val mensagemProdutoAdicionado = "Produto Adicionado"

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

                    if(response.code() == 422) {
                        Toast.makeText(context, mensagemProdutoNaoEncontrado, Toast.LENGTH_LONG).show()
                        return
                    }

                    validarResponse(response.message(), result!!, context)

                    produto.value = result;
                    Toast.makeText(context, mensagemProdutoAdicionado, Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<ProductViewModel>, t: Throwable) {
                    notificacaoFalha(context);
                }


            }
        )
    }
}