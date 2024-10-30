package com.example.imc

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "imc_table")
data class ImcEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val altura: Double,
    val peso: Double,
    val imc: Double,
    val genero: String
)