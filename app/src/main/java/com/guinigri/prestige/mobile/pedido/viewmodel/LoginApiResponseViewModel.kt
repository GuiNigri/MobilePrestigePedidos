package com.guinigri.prestige.mobile.pedido.viewmodel

import androidx.lifecycle.ViewModel

class LoginApiResponseViewModel(
    var status: Boolean?=null,
    var token:String?=null,
    var expires:String?=null,
    var message:String?=null
):ViewModel()