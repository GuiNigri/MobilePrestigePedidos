package com.guinigri.prestige.mobile.pedido

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.guinigri.prestige.mobile.pedido.settings.Token
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var file = Token.checkFile(applicationContext);
        if(file?.exists()!!){
            Token.readFile(file, applicationContext);
            var dateOffset = convertStringToDate(Token.expires);

            var result = dateOffset.compareTo(getDate())

            if(result >= 1){
                startActivity(
                    Intent(applicationContext, ApplicationActivity::class.java)
                )
            }
        }
    }

    private fun getDate():OffsetDateTime{
        return OffsetDateTime.now(ZoneOffset.UTC)
    }

    private fun convertStringToDate(date:String):OffsetDateTime{
        return OffsetDateTime.parse(date);
    }
}