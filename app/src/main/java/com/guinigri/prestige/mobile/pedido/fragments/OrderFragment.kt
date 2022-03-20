package com.guinigri.prestige.mobile.pedido.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.Result
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.guinigri.prestige.mobile.pedido.R
import com.guinigri.prestige.mobile.pedido.ViewModelFactory
import com.guinigri.prestige.mobile.pedido.adapter.ProdutoAdapter
import com.guinigri.prestige.mobile.pedido.viewmodel.produto.CallProductApiViewModel
import kotlinx.android.synthetic.main.fragment_order.*
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import java.util.jar.Manifest


class OrderFragment() : Fragment(), EasyPermissions.PermissionCallbacks {

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
        produtoApiViewModel.produto.value = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);

        configurarReciclerView()
        criarViewModel()

        btn_pesquisar_produto.setOnClickListener {
            progressBarAdicionarProduto.visibility = View.VISIBLE
            obterProdutoPeloCodigoBarras(txt_codigo_barras.text.toString())
        }

        produtoApiViewModel.produto.observe(viewLifecycleOwner, Observer { produto ->
            if(produto != null){
                var quantidade = if(txt_quantidade.text.toString() == "") 1 else txt_quantidade.text.toString().toInt()

                quantidade = if(quantidade < 1) 1 else quantidade

                adapter.adicionar(produto, quantidade)
            }

            progressBarAdicionarProduto.visibility = View.GONE
        })

        check_inserir_quantidade.setOnCheckedChangeListener { _, marcado ->
            txt_quantidade.isEnabled = marcado
            txt_quantidade.text = null
        }

        btn_codigo_barras.setOnClickListener {
            solicitarPermissaoCamera()
        }

        btn_prosseguir.setOnClickListener {
            if(adapter.itemCount <= 0)
                Toast.makeText(context, "Para prosseguir é necessario no minimo um produto adicionado.", Toast.LENGTH_LONG).show()
            else
                findNavController().navigate(R.id.empresaPedidoFragment)
        }

        atualizarValorTotal()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

            if (result?.contents != null) {
                acaoLeituraCodigoBarras(result.contents.toString())
                return
            }

            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun acaoLeituraCodigoBarras(codigoBarras: String){
        if(!check_inserir_quantidade.isChecked){
            obterProdutoPeloCodigoBarras(codigoBarras)
            iniciarLeituraCodigoBarras()
            return
        }

        txt_codigo_barras.setText(codigoBarras)

    }

    private fun obterProdutoPeloCodigoBarras(codigoBarras:String){
        produtoApiViewModel.obterProdutoPeloCodigoBarras(codigoBarras, requireContext())
    }

    private fun atualizarValorTotal(){
        ProdutoAdapter.valorTotalFormatado.observe(viewLifecycleOwner, Observer { valorTotal ->
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(
        requestCode: Int,
        perms: MutableList<String>) {

        iniciarLeituraCodigoBarras()
    }

    private fun iniciarLeituraCodigoBarras(){
        val scanner = IntentIntegrator.forSupportFragment(this)
        scanner.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        scanner.setBeepEnabled(false)
        scanner.initiateScan()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }

    private fun solicitarPermissaoCamera(){
        EasyPermissions.requestPermissions(
            PermissionRequest.Builder(this, 182, android.Manifest.permission.CAMERA)
                .setRationale("A permissão da câmera é necessaria para que o sistema consiga ler o codigo de barras")
                .setPositiveButtonText("Aceitar")
                .setNegativeButtonText("Cancelar").build()
        )
    }
}