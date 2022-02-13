package com.guinigri.prestige.mobile.pedido.viewmodel.produto

import android.os.Parcelable
import com.guinigri.prestige.mobile.pedido.viewmodel.BaseViewModel
import kotlinx.android.parcel.Parcelize

class ProductViewModel (
    var id:Int?=null,
    var referencia:String?=null,
    var cor:String?=null,
    var valorUnitario:Double?=null,
    var quantidade:Int = 1
): BaseViewModel()