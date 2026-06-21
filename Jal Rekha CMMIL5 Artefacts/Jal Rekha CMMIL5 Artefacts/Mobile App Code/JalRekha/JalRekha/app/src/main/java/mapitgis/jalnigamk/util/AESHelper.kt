package mapitgis.jalnigamk.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AESHelper {

    private const val AES_ALGORITHM = "AES/CBC/PKCS5Padding"
    private const val CHARSET = "UTF-8"
    private val IV = ByteArray(16) // 16-byte zero IV (same as your C# code)

    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(Exception::class)
    fun encrypt(plainText: String, encKey: String): String {
        return try {
            val cipher = Cipher.getInstance(AES_ALGORITHM)
            val keySpec = SecretKeySpec(encKey.toByteArray(Charset.forName(CHARSET)), "AES")
            val ivSpec = IvParameterSpec(IV)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
            val encrypted = cipher.doFinal(plainText.toByteArray(Charset.forName(CHARSET)))
            Base64.getEncoder().encodeToString(encrypted)
        } catch (ex: Exception) {
            throw IllegalArgumentException("Unable to encrypt. Invalid plain text. Error: ${ex.message}", ex)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(Exception::class)
    fun decrypt(cipherText: String, encKey: String): String {
        return try {
            val cipher = Cipher.getInstance(AES_ALGORITHM)
            val keySpec = SecretKeySpec(encKey.toByteArray(Charset.forName(CHARSET)), "AES")
            val ivSpec = IvParameterSpec(IV)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
            val decodedBytes = Base64.getDecoder().decode(cipherText)
            val decrypted = cipher.doFinal(decodedBytes)
            String(decrypted, Charset.forName(CHARSET))
        } catch (ex: Exception) {
            throw IllegalArgumentException("Unable to decrypt. Invalid cipher text. Error: ${ex.message}", ex)
        }
    }
}