package com.guinigri.prestige.mobile.pedido.api

import com.guinigri.prestige.mobile.pedido.api.service.ApplicationMainApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitClient {
    private val retrofit: Retrofit = Retrofit
        .Builder()
        .baseUrl("https://prestigedobrasilapi.azurewebsites.net")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getApplicationMainApiService():ApplicationMainApiService{
        return retrofit.create(ApplicationMainApiService::class.java);
    }
}