package com.guinigri.prestige.mobile.pedido.api.request.pedido

import androidx.lifecycle.ViewModel

class PagamentoDto(
    var formaPagamento:String?=null,
    var parcelas:String?=null,
    var frete:Double?=null,
    var desconto:Double?=null,
    var subtotal:Double?=null
): ViewModel()