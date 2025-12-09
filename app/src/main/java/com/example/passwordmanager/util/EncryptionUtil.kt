package com.example.passwordmanager.util

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object EncryptionUtil {

    // Simple static key for assignment. In real life, use Android Keystore.
    private const val SECRET_KEY = "1234567890123456" // 16 chars = 128-bit key

    private fun keySpec(): SecretKeySpec =
        SecretKeySpec(SECRET_KEY.toByteArray(Charsets.UTF_8), "AES")

    fun encrypt(plain: String): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, keySpec())
        val encrypted = cipher.doFinal(plain.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }

    fun decrypt(cipherText: String): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, keySpec())
        val decoded = Base64.decode(cipherText, Base64.DEFAULT)
        val decrypted = cipher.doFinal(decoded)
        return String(decrypted, Charsets.UTF_8)
    }
}
