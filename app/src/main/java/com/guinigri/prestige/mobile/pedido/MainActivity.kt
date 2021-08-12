package com.guinigri.prestige.mobile.pedido

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        checkTokenAndExpiresDate();

    }

    private fun checkTokenAndExpiresDate(){
        var fileToken = getFileToken();
        if(fileToken.exists()){
            Token.readFile(fileToken, applicationContext);
            var dateOffset = convertStringToDate(Token.getExpires());

            var result = dateOffset.compareTo(getDate())

            if(result >= 1){
                startActivity(
                    Intent(applicationContext, ApplicationActivity::class.java)
                )
            }
        }
    }

    private fun getFileToken(): File {
        return Token.checkFile(applicationContext)!!;
    }
    private fun getDate():OffsetDateTime{
        return OffsetDateTime.now(ZoneOffset.UTC)
    }

    private fun convertStringToDate(date:String):OffsetDateTime{
        return OffsetDateTime.parse(date);
    }
}