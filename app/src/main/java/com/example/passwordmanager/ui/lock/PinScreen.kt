package com.example.passwordmanager.ui.lock

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun PinScreen(
    hasExistingPin: Boolean,
    onCreatePin: (String) -> Unit,
    onValidatePin: (String) -> Boolean,
    onUnlockSuccess: () -> Unit
) {
    var step by remember { mutableStateOf(if (hasExistingPin) "enter" else "create") }
    var pin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }
    var showPin by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = when (step) {
                    "create" -> "Set a 6-digit PIN"
                    "confirm" -> "Confirm your PIN"
                    else -> "Enter PIN to unlock"
                },
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(24.dp))

            if (step == "create" || step == "enter") {
                PinField(
                    label = if (step == "create") "New PIN" else "PIN",
                    value = pin,
                    onValueChange = { value ->
                        if (value.length <= 6 && value.all { it.isDigit() }) {
                            pin = value
                        }
                    },
                    showPin = showPin,
                    onToggleShow = { showPin = !showPin }
                )
            } else if (step == "confirm") {
                PinField(
                    label = "Confirm PIN",
                    value = confirmPin,
                    onValueChange = { value ->
                        if (value.length <= 6 && value.all { it.isDigit() }) {
                            confirmPin = value
                        }
                    },
                    showPin = showPin,
                    onToggleShow = { showPin = !showPin }
                )
            }

            Spacer(Modifier.height(12.dp))

            errorText?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    when (step) {
                        "create" -> {
                            if (pin.length != 6) {
                                errorText = "PIN must be 6 digits"
                            } else {
                                errorText = null
                                step = "confirm"
                            }
                        }

                        "confirm" -> {
                            if (confirmPin != pin) {
                                errorText = "PINs do not match"
                            } else {
                                errorText = null
                                onCreatePin(pin)
                                onUnlockSuccess()
                            }
                        }

                        "enter" -> {
                            if (pin.length != 6) {
                                errorText = "PIN must be 6 digits"
                            } else {
                                val ok = onValidatePin(pin)
                                if (ok) {
                                    errorText = null
                                    onUnlockSuccess()
                                } else {
                                    errorText = "Incorrect PIN"
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = when (step) {
                        "create" -> "Continue"
                        "confirm" -> "Save PIN"
                        else -> "Unlock"
                    }
                )
            }
        }
    }
}

@Composable
private fun PinField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    showPin: Boolean,
    onToggleShow: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (showPin) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        trailingIcon = {
            TextButton(onClick = onToggleShow) {
                Text(if (showPin) "Hide" else "Show")
            }
        }
    )
}
