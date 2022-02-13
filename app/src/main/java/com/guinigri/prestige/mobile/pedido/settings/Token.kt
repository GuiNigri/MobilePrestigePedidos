package com.guinigri.prestige.mobile.pedido.settings

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import java.io.*
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class Token {
    companion object {
        private var token = ""
        private var validade = ""

        private fun deletarToken(file:File){
            file.delete();
        }

        fun obterDados(context : Context) {

            var diretorio = obterDiretorio(context);

            var arquivo = File("${diretorio}/token.note");

            if(arquivoNaoExiste(arquivo))
                return;

            lerDados(arquivo, context)
        }

        private fun arquivoNaoExiste(arquivo: File) : Boolean{
            return !arquivoExiste(arquivo);
        }

        private fun lerDados(arquivo: File, context: Context) {

            var arquivoDescriptografado = descriptografarArquivo(arquivo, context)

            val reader = BufferedReader(InputStreamReader(arquivoDescriptografado))

            var contador = 1

            reader.lines().forEach { line ->
                if(contador == 1){
                    this.token = line
                }else if(contador == 2) {
                    this.validade = line
                }

                contador += 1
            }

            arquivoDescriptografado.close()
        }

        private fun descriptografarArquivo(arquivo: File, context: Context) : FileInputStream {
            return EncryptedFile.Builder(
                arquivo, context, MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build().openFileInput()
        }

        fun validar() : Boolean{

            var dataAtual = OffsetDateTime.now(ZoneId.of("America/Sao_Paulo"));
            var data = OffsetDateTime.parse(this.validade);

            if(OffsetDateTime.now() <= OffsetDateTime.parse(this.validade))
                return true

            return false
        }

        fun atualizarDados(token: String, validade: String, context: Context){

            this.token = token
            this.validade = validade

            gravar(context)
        }

        private fun gravar(context: Context) {

            var diretorio = File(obterDiretorio(context));

            if(diretorioNaoExiste(diretorio))
                diretorio.mkdirs()

            var arquivo = File(diretorio.toString() + "/token.note")

            if(arquivoExiste(arquivo))
                arquivo.delete()

            var arquivoCriptografado = criptografarArquivo(arquivo, context)

            val writer = PrintWriter(arquivoCriptografado)

            writer.println(this.token)
            writer.println(this.validade)
            writer.flush()

            arquivoCriptografado.close()
        }

        private fun diretorioNaoExiste(diretorio: File) : Boolean{
            return arquivoNaoExiste(diretorio);
        }

        private fun arquivoExiste(arquivo: File) : Boolean{
            return arquivo.exists();
        }

        private fun criptografarArquivo(arquivo: File, context: Context): FileOutputStream{

            return EncryptedFile.Builder(
                arquivo, context, MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB).build().openFileOutput()
        }

        private fun obterDiretorio(context: Context): String {
            return context.filesDir!!.path + "/token"
        }

        fun getToken():String{
            return "Bearer $token";
        }
    }
}