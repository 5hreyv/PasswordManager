package com.example.passwordmanager.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {

    @Query("SELECT * FROM passwords ORDER BY id DESC")
    fun getAll(): Flow<List<PasswordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: PasswordEntity)

    @Update
    suspend fun update(entity: PasswordEntity)

    @Delete
    suspend fun delete(entity: PasswordEntity)

    @Query("SELECT * FROM passwords WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): PasswordEntity?
}
