package com.guinigri.prestige.mobile.pedido

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.guinigri.prestige.mobile.pedido.viewmodel.empresa.CallEmpresaApiViewModel
import com.guinigri.prestige.mobile.pedido.viewmodel.empresa.EmpresaApiResponse
import com.guinigri.prestige.mobile.pedido.viewmodel.produto.CallProductApiViewModel
import com.guinigri.prestige.mobile.pedido.viewmodel.produto.ProductViewModel
import kotlinx.android.synthetic.main.fragment_empresa_pedido.*

class EmpresaPedidoFragment : Fragment() {
    companion object {
        var empresa = MutableLiveData<EmpresaApiResponse>()
    }

    private lateinit var empresaApiViewModel: CallEmpresaApiViewModel
    private lateinit var viewModelFactory: ViewModelFactory

    private var empresaSelecionada = false
    private val MensagemEmpresaNaoInformada = "Para prosseguir é necessario Informar a empresa"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_empresa_pedido, container, false)
    }

    override fun onResume() {
        super.onResume()
        empresaApiViewModel.empresa.value = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);

        criarViewModel()

        btn_buscar_empresa.setOnClickListener {
            if(txt_cnpj.text.isNullOrEmpty())
                Toast.makeText(context, "O campo CNPJ é obrigatorio.", Toast.LENGTH_LONG).show()
            else
                buscarEmpresa()
        }

        empresaApiViewModel.empresa.observe(viewLifecycleOwner, Observer { empresaResponse ->
            if(empresaResponse != null){
                exibirEmpresa(empresaResponse)
                empresa.value = empresaResponse
            }
            else
            {
                exibirEmpresaNaoSelecionado()
                empresa.value = null
            }
        })

        btn_pagamento.setOnClickListener {
            prosseguirPagamento()
        }

    }

    private fun buscarEmpresa(){
        var cnpj = txt_cnpj.text.toString()
        empresaApiViewModel.obterEmpresaPeloCnpj(cnpj, requireContext())
    }

    private fun prosseguirPagamento(){
        if(empresaSelecionada)
            findNavController().navigate(R.id.pagamentoFragment)
        else
            Toast.makeText(context, MensagemEmpresaNaoInformada, Toast.LENGTH_LONG).show()
    }

    private fun exibirEmpresaNaoSelecionado(){
        txt_empresa_nome.setText("Nome: EMPRESA LTDA")
        txt_empresa_cnpj.setText("Cnpj: 00.000.000/0000-00")
        empresaSelecionada = false
    }

    private fun exibirEmpresa(empresa: EmpresaApiResponse){
        txt_empresa_nome.setText("Nome: ${empresa.razaoSocial}")
        txt_empresa_cnpj.setText("Cnpj: ${empresa.cnpj}")
        empresaSelecionada = true
    }

    private fun criarViewModel(){
        viewModelFactory = ViewModelFactory()
        activity?.let {
            empresaApiViewModel =
                ViewModelProvider(it, viewModelFactory) // MainActivity
                    .get(CallEmpresaApiViewModel::class.java)
        }
    }
}