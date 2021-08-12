package com.guinigri.prestige.mobile.pedido.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.guinigri.prestige.mobile.pedido.R
import com.guinigri.prestige.mobile.pedido.viewmodel.ProductViewModel

class ProductAdapter(
    var products: List<ProductViewModel> = listOf()
):RecyclerView.Adapter<ProductAdapter.ProdutoViewHolder>() {

    class ProdutoViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val card:ConstraintLayout = itemView.findViewById(R.id.ProdutoCard);
        val productInfo: TextView = itemView.findViewById(R.id.ProductInfo);
        val productQuantAndPrice: TextView = itemView.findViewById(R.id.ProductQuantAndPrice);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder{
        val card = LayoutInflater.from(parent.context).inflate(R.layout.item_pedido_card, parent, false);

        return ProdutoViewHolder(card);
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {

        holder.productInfo.text = "Ref: ${products[position].reference} - Cor: ${products[position].referenceColor} Vl. Unit: ${products[position].unitaryAmount}"
        holder.productQuantAndPrice.text = "Quant: ${products[position].quantity} - Vl. Total: ${products[position].unitaryAmount!! * products[position].quantity!!}"
    }

    fun updateData(produtos: List<ProductViewModel>){
        notifyDataSetChanged();
    }
}