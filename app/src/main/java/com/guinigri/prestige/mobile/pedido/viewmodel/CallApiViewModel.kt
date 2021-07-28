package com.guinigri.prestige.mobile.pedido.viewmodel

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guinigri.prestige.mobile.pedido.api.RetroFitClient
import com.guinigri.prestige.mobile.pedido.fragments.LoginFragment
import com.guinigri.prestige.mobile.pedido.settings.Token
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CallApiViewModel:ViewModel() {
    var status = MutableLiveData<Boolean>();

    fun logar(email:String, password:String, context: Context){
        var json:String  =  "{\"email\": \"$email\", \"password\": \"$password\"}";
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
                        Toast.makeText(context, "Usuario ou senha incorretos", Toast.LENGTH_LONG).show()
                        status.value = false;
                    }
                    else{
                        if(result?.status!!){
                            Token.token = result?.token!!;
                            Token.expires = result?.expires!!;
                            status.value = true;
                            Token.saveToken(context)
                        }
                    }
                }

                override fun onFailure(call: Call<LoginApiResponseViewModel>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                    status.value = false;
                }


            }
        )
    }
}