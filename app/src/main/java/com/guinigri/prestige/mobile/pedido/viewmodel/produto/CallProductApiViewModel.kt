package com.guinigri.prestige.mobile.pedido.viewmodel

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.guinigri.prestige.mobile.pedido.api.RetroFitClient
import com.guinigri.prestige.mobile.pedido.settings.Token
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CallProductApiViewModel:ViewModel() {

    fun getProduct(barCode:Long, context:Context){
        var call = RetroFitClient.getApplicationMainApiService().getProduct(barCode,Token.getToken());

        call.enqueue(
            object : Callback<ProductViewModel> {

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<ProductViewModel>,
                    response: Response<ProductViewModel>
                ) {
                    var result = response.body();


                    if(result == null && response.message() == "Unauthorized"){
                        Toast.makeText(context, "Usuario ou senha incorretos", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ProductViewModel>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                }


            }
        )
    }
}