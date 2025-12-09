package com.example.passwordmanager.data

import com.example.passwordmanager.util.EncryptionUtil
import kotlinx.coroutines.flow.Flow

class PasswordRepository(private val dao: PasswordDao) {

    fun observeAll(): Flow<List<PasswordEntity>> = dao.getAll()

    suspend fun add(account: String, username: String, password: String) {
        val encrypted = EncryptionUtil.encrypt(password)
        dao.insert(
            PasswordEntity(
                accountType = account,
                username = username,
                encryptedPassword = encrypted
            )
        )
    }

    suspend fun update(entity: PasswordEntity, account: String, username: String, password: String) {
        val encrypted = EncryptionUtil.encrypt(password)
        dao.update(
            entity.copy(
                accountType = account,
                username = username,
                encryptedPassword = encrypted
            )
        )
    }

    suspend fun delete(entity: PasswordEntity) = dao.delete(entity)

    suspend fun getById(id: Int): PasswordEntity? = dao.getById(id)
}
