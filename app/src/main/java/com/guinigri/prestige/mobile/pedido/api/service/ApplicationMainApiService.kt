package com.guinigri.prestige.mobile.pedido.api.service

import com.guinigri.prestige.mobile.pedido.viewmodel.LoginApiRequestViewModel
import com.guinigri.prestige.mobile.pedido.viewmodel.LoginApiResponseViewModel
import com.guinigri.prestige.mobile.pedido.viewmodel.ProductViewModel
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApplicationMainApiService {

    @POST("/api/usuario/login")
    fun login(@Body body:RequestBody):Call<LoginApiResponseViewModel>

    @GET("/api/produto")
    fun getProduct(@Query("barcode") barCode:Long, @Header("Authorization") key:String):Call<ProductViewModel>
}