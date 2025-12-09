package com.example.passwordmanager.util

import android.content.Context

class PinStorage(context: Context) {

    private val prefs = context.getSharedPreferences("secure_prefs", Context.MODE_PRIVATE)
    private val keyPin = "encrypted_pin"

    fun hasPin(): Boolean = prefs.contains(keyPin)

    fun savePin(pin: String) {
        val encrypted = EncryptionUtil.encrypt(pin)
        prefs.edit().putString(keyPin, encrypted).apply()
    }

    fun validatePin(pin: String): Boolean {
        val stored = prefs.getString(keyPin, null) ?: return false
        val decrypted = EncryptionUtil.decrypt(stored)
        return decrypted == pin
    }

    fun clearPin() {
        prefs.edit().remove(keyPin).apply()
    }
}
