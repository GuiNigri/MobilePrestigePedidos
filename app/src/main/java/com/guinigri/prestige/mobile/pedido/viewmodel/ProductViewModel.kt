package com.guinigri.prestige.mobile.pedido.viewmodel

import androidx.lifecycle.ViewModel

class ProductViewModel(
    var reference:String?=null,
    var referenceColor:String?=null,
    var unitaryAmount:Double?=null,
    var quantity:Int?=null
): ViewModel()