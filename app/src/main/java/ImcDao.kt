package com.example.imc

import androidx.room.*

@Dao
interface ImcDao {

    // Create
    @Insert
    suspend fun insert(imc: ImcEntity): Long

    // Read (buscando todos os registros)
    @Query("SELECT * FROM imc_table")
    suspend fun getAllImc(): List<ImcEntity>

    // Update
    @Update
    suspend fun update(imcData: ImcEntity)

    // Delete
    @Delete
    suspend fun delete(imcData: ImcEntity)
}
