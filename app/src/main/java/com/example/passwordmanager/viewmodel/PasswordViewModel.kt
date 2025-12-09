package com.example.passwordmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.passwordmanager.data.PasswordEntity
import com.example.passwordmanager.data.PasswordRepository
import com.example.passwordmanager.util.EncryptionUtil
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PasswordViewModel(
    private val repository: PasswordRepository
) : ViewModel() {

    val passwords: StateFlow<List<PasswordEntity>> =
        repository.observeAll()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addPassword(account: String, username: String, password: String) {
        viewModelScope.launch {
            repository.add(account, username, password)
        }
    }

    fun updatePassword(entity: PasswordEntity, account: String, username: String, password: String) {
        viewModelScope.launch {
            repository.update(entity, account, username, password)
        }
    }

    fun deletePassword(entity: PasswordEntity) {
        viewModelScope.launch {
            repository.delete(entity)
        }
    }

    fun decryptedPassword(entity: PasswordEntity): String =
        EncryptionUtil.decrypt(entity.encryptedPassword)

    companion object {
        class Factory(private val repository: PasswordRepository) : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PasswordViewModel(repository) as T
            }
        }
    }
}
