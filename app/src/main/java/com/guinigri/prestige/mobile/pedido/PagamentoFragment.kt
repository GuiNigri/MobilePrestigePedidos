package com.guinigri.prestige.mobile.pedido

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.guinigri.prestige.mobile.pedido.adapter.ProdutoAdapter
import com.guinigri.prestige.mobile.pedido.api.request.pedido.PagamentoDto
import com.guinigri.prestige.mobile.pedido.api.request.pedido.ProdutoDto
import com.guinigri.prestige.mobile.pedido.api.request.pedido.RegistrarPedidoRequest
import com.guinigri.prestige.mobile.pedido.viewmodel.empresa.CallEmpresaApiViewModel
import com.guinigri.prestige.mobile.pedido.viewmodel.pedido.CallPedidoApiViewModel
import com.guinigri.prestige.mobile.pedido.viewmodel.produto.CallProductApiViewModel
import kotlinx.android.synthetic.main.fragment_empresa_pedido.*
import kotlinx.android.synthetic.main.fragment_pagamento.*
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class PagamentoFragment : Fragment() {

    private lateinit var pedidoApiViewModel: CallPedidoApiViewModel
    private lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pagamento, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        criarViewModel()

        atualizarValorTela(calcularValorTotal())

        observarAtualizacaoValores()

        btn_finalizar.setOnClickListener {
            if(submissaoNaoEhValida())
                Toast.makeText(context, "Todos os campos são obrigatorios", Toast.LENGTH_LONG).show()
            else
                finalizarPedido()
        }

        pedidoApiViewModel.numeroPedido.observe(viewLifecycleOwner, androidx.lifecycle.Observer { pedidoResponse ->
            if(pedidoResponse != null){
                navegarTelaSucesso(pedidoResponse.numeroPedido!!)
                resetarNaConclusaoPedido()
            }
        })
    }

    private fun resetarNaConclusaoPedido(){
        ProdutoAdapter.resetar()
        pedidoApiViewModel.numeroPedido.value = null
    }

    private fun navegarTelaSucesso(numeroPedido:Int){
        val action = PagamentoFragmentDirections.actionPagamentoFragmentToPedidoSucessoFragment(numeroPedido)
        findNavController().navigate(action)
    }

    private fun submissaoNaoEhValida(): Boolean{
        return (txt_parcelas.text.isNullOrEmpty() || txt_desconto.text.isNullOrEmpty() || txt_frete.text.isNullOrEmpty())
    }
    private fun finalizarPedido(){
        var request = montarRequest()
        pedidoApiViewModel.registrarPedido(request, requireContext())
    }

    private fun montarRequest() : RegistrarPedidoRequest{
        var produtosDto = mutableListOf<ProdutoDto>()
        var empresa = EmpresaPedidoFragment.empresa.value!!.id
        var formaPagamento = txt_forma_pagamento.selectedItem.toString().lowercase()
        var subtotal = ProdutoAdapter.valorTotalSemFormatacao.value
        var observacoes = txt_observacoes.text.toString()

        var pagamentoDto = PagamentoDto(
            formaPagamento,
            txt_parcelas.text.toString(),
            txt_frete.text.toString().toDouble(),
            txt_desconto.text.toString().toDouble(),
            subtotal)

        ProdutoAdapter.produtos.forEach { produto ->
            var produtoDto = ProdutoDto(produto.id, produto.quantidade)
            produtosDto.add(produtoDto)
        }

        return RegistrarPedidoRequest(empresa, produtosDto, pagamentoDto, observacoes)
    }

    private fun criarViewModel(){
        viewModelFactory = ViewModelFactory()
        activity?.let {
            pedidoApiViewModel =
                ViewModelProvider(it, viewModelFactory) // MainActivity
                    .get(CallPedidoApiViewModel::class.java)
        }
    }

    private fun calcularValorTotal(): Double{
        var subtotal = ProdutoAdapter.valorTotalSemFormatacao.value!!.toDouble()
        var frete = if(txt_frete.text.isNullOrEmpty()) 0.0 else txt_frete.text.toString().toDouble()
        var desconto = if(txt_desconto.text.isNullOrEmpty()) 0.0 else txt_desconto.text.toString().toDouble()

        return subtotal + frete - desconto
    }

    private fun observarAtualizacaoValores(){

        txt_frete.doAfterTextChanged {
            atualizarValorTela(calcularValorTotal())
        }

        txt_desconto.doAfterTextChanged {
            atualizarValorTela(calcularValorTotal())
        }
    }

    private fun atualizarValorTela(valor: Double){
        txt_valor_total_pagamento.text = "Valor: R$ ${formatarParaRealBrl(valor)}"
    }

    private fun formatarParaRealBrl(valor: Double): String{
        val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

        val formatter = numberFormat as DecimalFormat
        formatter.applyPattern("###,###,##0.00")

        return formatter.format(valor)
    }
}