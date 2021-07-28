package com.guinigri.prestige.mobile.pedido.settings

import android.content.Context
import android.widget.Toast
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.util.*

class Token {
    companion object {
        var token = ""
        var expires = ""

        private fun obterDiretorio(diretorio: String, criar: Boolean, context: Context): File {
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

        private fun checkFile(context:Context){
            var directory = obterDiretorio("token", false,context);
            var file = File("${directory.path}/token.note");

            if(file.exists()){
                deleteToken(file);
            }
        }
        fun saveToken(context: Context) {
            checkFile(context);

            var diretorio = obterDiretorio("token", false, context)

            val masterKeyAlias: String =
                MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

            var nota = File(diretorio.path + "/token.note")

            var encryptedOut = EncryptedFile.Builder(
                nota, context, masterKeyAlias,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB

            ).build().openFileOutput()

            val pw = PrintWriter(encryptedOut)

            pw.println(this.token)
            pw.println(this.expires)
            pw.flush()

            encryptedOut.close()
        }
    }
}