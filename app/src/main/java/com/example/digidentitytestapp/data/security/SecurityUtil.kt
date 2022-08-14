package com.example.digidentitytestapp.data.security

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

// TODO: extract to interface
object SecurityUtil {

    fun encrypt(target: String, secretKey: String, vector: String): String {
        val kb = Base64.decode(secretKey, Base64.NO_WRAP)
        val vb = Base64.decode(vector, Base64.NO_WRAP)
        val secretKeySpec = SecretKeySpec(kb, "AES")
        val ivSpec = IvParameterSpec(vb)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding").apply {
            init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec)
        }
        val encryptedBytes = cipher.doFinal(target.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
    }

    fun decrypt(target: String, secretKey: String, vector: String): String {
        val kb = Base64.decode(secretKey, Base64.NO_WRAP)
        val vb = Base64.decode(vector, Base64.NO_WRAP)
        val encryptedBytes = Base64.decode(target, Base64.NO_WRAP)

        val secretKeySpec = SecretKeySpec(kb, "AES")
        val ivSpec = IvParameterSpec(vb)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding").apply {
            init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec)
        }
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes)
    }
}