package com.guinigri.prestige.mobile.pedido.api.service

import com.guinigri.prestige.mobile.pedido.viewmodel.login.LoginApiResponseViewModel
import com.guinigri.prestige.mobile.pedido.viewmodel.produto.ProductViewModel
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApplicationMainApiService {

    @POST("/api/usuario/login")
    fun login(@Body body:RequestBody):Call<LoginApiResponseViewModel>

    @GET("/api/produto")
    fun obterProdutoPeloCodigoBarras(@Query("request") request:String, @Header("Authorization") key:String):Call<ProductViewModel>
}