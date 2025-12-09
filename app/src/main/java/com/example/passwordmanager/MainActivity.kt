package com.example.passwordmanager

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.passwordmanager.data.AppDatabase
import com.example.passwordmanager.data.PasswordRepository
import com.example.passwordmanager.ui.home.HomeScreen
import com.example.passwordmanager.ui.lock.PinScreen
import com.example.passwordmanager.ui.theme.PasswordManagerTheme
import com.example.passwordmanager.util.PinStorage
import com.example.passwordmanager.viewmodel.PasswordViewModel

class MainActivity : FragmentActivity() {   // ✔ FIXED: extend FragmentActivity

    private lateinit var pinStorage: PinStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pinStorage = PinStorage(this)

        // shared state between Activity + Compose
        val isUnlockedState = mutableStateOf(false)

        // Try biometric first IF a PIN exists AND biometric is available
        if (pinStorage.hasPin() && canUseBiometric()) {
            showBiometricPrompt(
                onSuccess = { isUnlockedState.value = true },
                onFallback = { /* PIN screen will be shown */ }
            )
        }

        setContent {
            PasswordManagerApp(
                isUnlockedState = isUnlockedState,
                pinStorage = pinStorage
            )
        }
    }

    private fun canUseBiometric(): Boolean {
        val biometricManager = BiometricManager.from(this)
        val result = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        )
        return result == BiometricManager.BIOMETRIC_SUCCESS
    }

    private fun showBiometricPrompt(
        onSuccess: () -> Unit,
        onFallback: () -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(this)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onFallback()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }
        }

        // ✔ Correct constructor for Narwhal
        val biometricPrompt = BiometricPrompt(this, executor, callback)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock Password Manager")
            .setSubtitle("Use your fingerprint or face")
            .setNegativeButtonText("Use PIN instead")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()

        biometricPrompt.authenticate(promptInfo)   // ✔ Now works
    }
}

@Composable
fun PasswordManagerApp(
    isUnlockedState: MutableState<Boolean>,
    pinStorage: PinStorage
) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }
    val repo = remember { PasswordRepository(db.passwordDao()) }
    val viewModel: PasswordViewModel = viewModel(
        factory = PasswordViewModel.Companion.Factory(repo)
    )

    PasswordManagerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            if (!isUnlockedState.value) {
                // PIN screen handles create OR enter PIN
                PinScreen(
                    hasExistingPin = pinStorage.hasPin(),
                    onCreatePin = { newPin ->
                        pinStorage.savePin(newPin)
                    },
                    onValidatePin = { enteredPin ->
                        pinStorage.validatePin(enteredPin)
                    },
                    onUnlockSuccess = {
                        isUnlockedState.value = true
                    }
                )
            } else {
                HomeScreen(viewModel = viewModel)
            }
        }
    }
}
