package com.guinigri.prestige.mobile.pedido

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.guinigri.prestige.mobile.pedido.settings.Token
import java.io.File
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        validarToken();

    }

    private fun validarToken(){
        try
        {
            Token.obterDados(applicationContext)!!;

            if(Token.validar()){
                startActivity(Intent(applicationContext, ApplicationActivity::class.java))
                finish()
            }
        }
        catch (ex : Exception)
        {
            println("Erro ao ler token ou token inexistente.")
        }

    }
}