package com.guinigri.prestige.mobile.pedido

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.guinigri.prestige.mobile.pedido.viewmodel.login.CallLoginApiViewModel
import com.guinigri.prestige.mobile.pedido.viewmodel.produto.CallProductApiViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_order.*

class OrderFragment : Fragment() {

    private lateinit var produtoApiViewModel: CallProductApiViewModel
    private lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);

        viewModelFactory = ViewModelFactory()
        activity?.let {
            produtoApiViewModel =
                ViewModelProvider(it, viewModelFactory) // MainActivity
                    .get(CallProductApiViewModel::class.java)

            btn_pesquisar_produto.setOnClickListener {
                produtoApiViewModel.obterProdutoPeloCodigoBarras(txt_codigo_barras.text.toString(), requireContext())
            }
        }
    }
}