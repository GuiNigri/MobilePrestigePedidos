package com.guinigri.prestige.mobile.pedido.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.guinigri.prestige.mobile.pedido.R
import kotlinx.android.synthetic.main.fragment_empresa_pedido.*
import kotlinx.android.synthetic.main.fragment_pedido_sucesso.*

class PedidoSucessoFragment : Fragment() {

    val argumento: PedidoSucessoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pedido_sucesso, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            retornarMenuPrincipal()
        }

        btn_voltar_menu.setOnClickListener {
            retornarMenuPrincipal()
        }

        atualizarNumeroPedidoEmTela()
    }

    private fun retornarMenuPrincipal(){
        findNavController().navigate(R.id.menuFragment)
    }

    private fun atualizarNumeroPedidoEmTela(){
        txt_numero_pedido.text = "Pedido: ${argumento.numeroPedido}"
    }
}