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
import com.guinigri.prestige.mobile.pedido.viewmodel.CallApiViewModel
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private lateinit var apiViewModel: CallApiViewModel
    private lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState);

        viewModelFactory = ViewModelFactory()
        activity?.let {
            apiViewModel =
                ViewModelProvider(it, viewModelFactory) // MainActivity
                    .get(CallApiViewModel::class.java)
        }

        btn_entrar.setOnClickListener {
            var email = txtEmail.text.toString();
            var password = txtPassword.text.toString();

            if(email.isNotEmpty() && password.isNotEmpty()){
                progressBarLogin.visibility = View.VISIBLE
                apiViewModel.logar(email, password, requireContext());

                apiViewModel.status.observe(viewLifecycleOwner, Observer { status ->
                    if (status){
                        requireActivity().finish()
                        startActivity(
                            Intent(activity, ApplicationActivity::class.java)
                        )
                    }
                    progressBarLogin.visibility = View.GONE
                })


            }
            else{
                Toast.makeText(
                    requireContext(),
                    "Todos os campos devem ser preenchidos",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
        btn_return.setOnClickListener {
            findNavController().navigate(R.id.homeFragment);
        }
    }

}