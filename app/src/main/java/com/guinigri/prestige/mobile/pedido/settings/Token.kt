package com.guinigri.prestige.mobile.pedido.settings

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import java.io.*
import java.util.*

class Token {
    companion object {
        var token = ""
        var expires = ""

        private fun getDirectory(diretorio: String, criar: Boolean, context: Context): File {
            var dirArq = context.filesDir!!.path + "/" + diretorio
            Toast.makeText(context, dirArq, Toast.LENGTH_LONG).show()
            var dirFile = File(dirArq)
            if (!dirFile.exists() && (!criar || !dirFile.mkdirs()))
                throw Exception("Diretório indisponível")
            return dirFile
        }

        private fun deleteToken(file:File){
            file.delete();
        }

        fun checkFile(context:Context): File? {
            var directory = getDirectory("token", false,context);
            var file = File("${directory.path}/token.note");

            if(file.exists()){
                return file;
            }

            return null;
        }
        fun readFile(nome: File, context: Context){

            var encryptedIn = EncryptedFile.Builder(
                nome, context, encrypt(),
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build().openFileInput()

            val br = BufferedReader(InputStreamReader(encryptedIn))

            var count = 1
            br.lines().forEach{ line ->
                if(count == 1){
                    this.token = line
                }else if(count == 2) {
                    this.expires = line
                }
                count += 1
            }

            encryptedIn.close()
        }

        fun saveToken(context: Context) {
            var file = checkFile(context);

            if(file?.exists()!!){
              deleteToken(file!!);
            }

            var diretorio = getDirectory("token", false, context)

            var note = File(diretorio.path + "/token.note")

            var encryptedOut = EncryptedFile.Builder(
                note, context, encrypt(),
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB

            ).build().openFileOutput()

            val pw = PrintWriter(encryptedOut)

            pw.println(this.token)
            pw.println(this.expires)
            pw.flush()

            encryptedOut.close()
        }

        private fun encrypt():String{
            return MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        }
    }
}