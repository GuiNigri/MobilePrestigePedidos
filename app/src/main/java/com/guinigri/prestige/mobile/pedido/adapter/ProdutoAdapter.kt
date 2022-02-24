package com.guinigri.prestige.mobile.pedido.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.guinigri.prestige.mobile.pedido.R
import com.guinigri.prestige.mobile.pedido.viewmodel.produto.ProductViewModel
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class ProdutoAdapter(
):RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder>() {

    companion object {
        var produtos = mutableListOf<ProductViewModel>()
        var valorTotalFormatado = MutableLiveData<String>()
        var valorTotalSemFormatacao = MutableLiveData<Double>()

        fun resetar(){
            produtos.clear()
            valorTotalFormatado.value = "0,00"
            valorTotalSemFormatacao.value = 0.0
        }
    }

    class ProdutoViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val card:ConstraintLayout = itemView.findViewById(R.id.ProdutoCard);
        val productInfo: TextView = itemView.findViewById(R.id.ProductInfo);
        val productQuantAndPrice: TextView = itemView.findViewById(R.id.ProductQuantAndPrice);
        val btnDeletar: ImageButton = itemView.findViewById(R.id.btn_deletar);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder{
        val card = LayoutInflater.from(parent.context).inflate(R.layout.item_pedido_card, parent, false);

        return ProdutoViewHolder(card);
    }

    override fun getItemCount() = produtos.size

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {

        var valorUnitarioFormatado = formatarParaRealBrl(produtos[position].valorUnitario!!)

        holder.productInfo.text = "Ref: ${produtos[position].referencia} - Cor: ${produtos[position].cor} | Vl. Unit: R$ ${valorUnitarioFormatado}"
        holder.productQuantAndPrice.text = "Quant: ${produtos[position].quantidade}"

        holder.btnDeletar.setOnClickListener {
            var id = produtos[position].id!!

            deletar(id)
            notifyDataSetChanged();
            atualizarValorTotal()
        }
    }

    fun adicionar(produto: ProductViewModel, quantidade: Int){
        adicionarCarrinho(produto, quantidade)
        notifyDataSetChanged();
        atualizarValorTotal()
    }

    fun atualizarProdutos(produtosSalvos: MutableList<ProductViewModel>){
        produtos.addAll(produtosSalvos)
        notifyDataSetChanged();
        atualizarValorTotal()
    }

    private fun adicionarCarrinho(produtoAdicionado: ProductViewModel, quantidade: Int){

        var produto = obterProduto(produtoAdicionado.id!!)

        if(produto != null){
            produto.quantidade += quantidade
            return
        }

        produtoAdicionado.quantidade = quantidade
        produtos.add(produtoAdicionado)
    }

    private fun atualizarValorTotal(){
        valorTotalFormatado.value = formatarParaRealBrl(calcularValorTotal())
        valorTotalSemFormatacao.value = calcularValorTotal()
    }

    private fun calcularValorTotal() : Double{
        return produtos.sumOf { x -> x.quantidade * x.valorUnitario!! }
    }

    fun deletar(id: Int){
        var produto = obterProduto(id)

        if(produto != null)
            produtos.remove(produto)
    }

    private fun obterProduto(id: Int) : ProductViewModel?{
        return produtos.find { x -> x.id == id }
    }

    fun obterProdutos() : MutableList<ProductViewModel>{
        return produtos;
    }

    fun deletarTodosProdutos(){
        produtos.clear()
    }

    private fun formatarParaRealBrl(valor: Double): String{
        val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

        val formatter = numberFormat as DecimalFormat
        formatter.applyPattern("###,###,##0.00")

        return formatter.format(valor)
    }
}