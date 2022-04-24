package com.guinigri.prestige.mobile.pedido.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.guinigri.prestige.mobile.pedido.ApplicationActivity
import com.guinigri.prestige.mobile.pedido.R
import com.guinigri.prestige.mobile.pedido.ViewModelFactory
import com.guinigri.prestige.mobile.pedido.viewmodel.login.CallLoginApiViewModel
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private lateinit var loginApiViewModel: CallLoginApiViewModel
    private var viewModelFactory = ViewModelFactory()

    private val mensagemCamposNaoPreenchidos = "Todos os campos devem ser preenchidos"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);

        criarViewModel()

        btn_entrar.setOnClickListener {
            var email = txtEmail.text.toString();
            var senha = txtPassword.text.toString();

            if(email.isNotEmpty() && senha.isNotEmpty()) {
                progressBarLogin.visibility = View.VISIBLE
                autenticar(email, senha)
            }
            else
                exibirNotificacao(mensagemCamposNaoPreenchidos)
        }

        btn_return.setOnClickListener {
            findNavController().navigate(R.id.homeFragment);
        }
    }

    private fun exibirNotificacao(mensagem : String) {
        Toast.makeText(
            requireContext(),
            mensagem,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun autenticar(email: String, senha: String) {
        loginApiViewModel.logar(email, senha, requireContext());

        loginApiViewModel.status.observe(viewLifecycleOwner, Observer { status ->
            if (status){
                requireActivity().finish()

                startActivity(Intent(activity, ApplicationActivity::class.java))
            }

            progressBarLogin.visibility = View.GONE
        })
    }

    private fun criarViewModel() {

        loginApiViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory) // MainActivity
                .get(CallLoginApiViewModel::class.java)
    }
}