package com.guinigri.prestige.mobile.pedido.fragments

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
import com.guinigri.prestige.mobile.pedido.R
import com.guinigri.prestige.mobile.pedido.ViewModelFactory
import com.guinigri.prestige.mobile.pedido.viewmodel.empresa.CallEmpresaApiViewModel
import com.guinigri.prestige.mobile.pedido.viewmodel.empresa.EmpresaApiResponse
import kotlinx.android.synthetic.main.fragment_empresa_pedido.*

class EmpresaPedidoFragment : Fragment() {

    companion object {
        var empresa = MutableLiveData<EmpresaApiResponse>()
    }

    private lateinit var empresaApiViewModel: CallEmpresaApiViewModel
    private var viewModelFactory = ViewModelFactory()

    private var empresaSelecionada = false
    private val mensagemEmpresaNaoInformada = "Para prosseguir é necessario Informar a empresa"
    private val mensagemCnpjNaoInformado = "O campo CNPJ é obrigatorio."

    private val nomeEmpresaPadrao = "Nome: EMPRESA LTDA"
    private val cnpjEmpresaPadrao = "Cnpj: 00.000.000/0000-00"

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
            if (txt_cnpj.text.isNullOrEmpty())
                Toast.makeText(context, mensagemCnpjNaoInformado, Toast.LENGTH_LONG).show()
            else
                buscarEmpresa()
        }

        empresaApiViewModel.empresa.observe(viewLifecycleOwner, Observer { empresaResponse ->
            atualizarDadosEmpresa(empresaResponse)
        })

        btn_pagamento.setOnClickListener {
            prosseguirPagamento()
        }
    }

    private fun atualizarDadosEmpresa(empresaResponse: EmpresaApiResponse?) {
        if(empresaResponse != null) {
            exibirEmpresa(empresaResponse)
            empresa.value = empresaResponse
        }
        else {
            restaurarExibicaoEmpresa()
            empresa.value = null
        }
    }

    private fun buscarEmpresa() {
        var cnpj = txt_cnpj.text.toString()
        empresaApiViewModel.obterEmpresaPeloCnpj(cnpj, requireContext())
    }

    private fun prosseguirPagamento() {
        if(empresaSelecionada)
            findNavController().navigate(R.id.pagamentoFragment)
        else
            Toast.makeText(context, mensagemEmpresaNaoInformada, Toast.LENGTH_LONG).show()
    }

    private fun restaurarExibicaoEmpresa() {
        txt_empresa_nome.text = nomeEmpresaPadrao
        txt_empresa_cnpj.text = cnpjEmpresaPadrao
        empresaSelecionada = false
    }

    private fun exibirEmpresa(empresa: EmpresaApiResponse) {
        txt_empresa_nome.text = "Nome: ${empresa.razaoSocial}"
        txt_empresa_cnpj.text = "Cnpj: ${empresa.cnpj}"
        empresaSelecionada = true
    }

    private fun criarViewModel() {
        empresaApiViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)
                .get(CallEmpresaApiViewModel::class.java)
    }
}