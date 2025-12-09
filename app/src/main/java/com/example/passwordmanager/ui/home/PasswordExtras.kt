package com.example.passwordmanager.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.random.Random

data class PasswordStrength(val label: String, val score: Int)

fun calculatePasswordStrength(password: String): PasswordStrength {
    if (password.isEmpty()) return PasswordStrength("Empty", 0)

    var score = 0
    if (password.length >= 8) score++
    if (password.any { it.isDigit() }) score++
    if (password.any { it.isUpperCase() }) score++
    if (password.any { !it.isLetterOrDigit() }) score++

    val label = when (score) {
        0, 1 -> "Weak"
        2 -> "Medium"
        3 -> "Strong"
        else -> "Very Strong"
    }
    return PasswordStrength(label, score)
}

@Composable
fun PasswordStrengthMeter(password: String) {
    val strength = calculatePasswordStrength(password)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        ) {
            repeat(4) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(end = if (index == 3) 0.dp else 2.dp)
                        .background(
                            if (index < strength.score)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Strength: ${strength.label}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

fun generateStrongPassword(length: Int = 16): String {
    val chars =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#\$%^&*()_+-="
    return (1..length)
        .map { chars[Random.nextInt(chars.length)] }
        .joinToString("")
}

@Composable
fun PasswordGeneratorSection(onGenerated: (String) -> Unit) {
    Spacer(Modifier.height(8.dp))
    Button(
        onClick = { onGenerated(generateStrongPassword()) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Generate Strong Password")
    }
}
