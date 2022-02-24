package com.guinigri.prestige.mobile.pedido.viewmodel.empresa

import com.guinigri.prestige.mobile.pedido.viewmodel.BaseViewModel

class EmpresaApiResponse(
    var id:Int?=null,
    var razaoSocial:String?=null,
    var cnpj:String?=null,
): BaseViewModel()