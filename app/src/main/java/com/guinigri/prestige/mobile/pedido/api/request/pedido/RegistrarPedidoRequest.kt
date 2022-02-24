package com.guinigri.prestige.mobile.pedido.api.request.pedido

import androidx.lifecycle.ViewModel

class RegistrarPedidoRequest (
    var empresa:Int?=null,
    var produtos:List<ProdutoDto>?=null,
    var pagamento:PagamentoDto?=null,
    var observacoes:String?=null
): ViewModel()
