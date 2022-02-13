package com.guinigri.prestige.mobile.pedido

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.guinigri.prestige.mobile.pedido.adapter.ProdutoAdapter
import com.guinigri.prestige.mobile.pedido.viewmodel.Carrinho
import com.guinigri.prestige.mobile.pedido.viewmodel.produto.CallProductApiViewModel
import com.guinigri.prestige.mobile.pedido.viewmodel.produto.ProductViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_order.*
import kotlinx.android.synthetic.main.item_pedido_card.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class OrderFragment() : Fragment() {

    private lateinit var produtoApiViewModel: CallProductApiViewModel
    private lateinit var viewModelFactory: ViewModelFactory

    var adapter = ProdutoAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onPause() {
        super.onPause()
        salvarEstadoProdutos()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);

        configurarReciclerView()
        criarViewModel()

        recuperarEstadoProdutos()

        btn_pesquisar_produto.setOnClickListener {
            produtoApiViewModel.obterProdutoPeloCodigoBarras(txt_codigo_barras.text.toString(), requireContext())
        }

        produtoApiViewModel.produto.observe(viewLifecycleOwner, Observer { produto ->
            if(produto != null){
                var quantidade = if(txt_quantidade.text.toString() == "") 1 else txt_quantidade.text.toString().toInt()

                quantidade = if(quantidade < 1) 1 else quantidade

                adapter.adicionar(produto, quantidade)
            }
        })

        atualizarValorTotal()
    }

    private fun atualizarValorTotal(){
        adapter.valorTotal.observe(viewLifecycleOwner, Observer { valorTotal ->
            txt_valor_total.setText("Subtotal: R$ ${valorTotal}")
        })
    }

    private fun configurarReciclerView(){
        produtosList.layoutManager = LinearLayoutManager(activity)
        produtosList.adapter = adapter
    }

    private fun criarViewModel(){
        viewModelFactory = ViewModelFactory()
        activity?.let {
            produtoApiViewModel =
                ViewModelProvider(it, viewModelFactory) // MainActivity
                    .get(CallProductApiViewModel::class.java)
        }
    }

    private fun salvarEstadoProdutos(){
        Carrinho.produtos.addAll(adapter.obterProdutos())
        produtoApiViewModel.produto.value = null
        adapter.deletarTodosProdutos()
    }

    private fun recuperarEstadoProdutos(){
        adapter.atualizarProdutos(Carrinho.produtos)
        Carrinho.produtos.clear()
    }
}