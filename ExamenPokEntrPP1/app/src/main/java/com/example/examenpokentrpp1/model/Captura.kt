package com.example.examenpokentrpp1.model

import androidx.room.*


@Entity(tableName = "captura_table")
class Captura (

    @PrimaryKey(autoGenerate = true)
    val idCaptura: Int,
    val idBaseEntrenador: Int,
    val idBasePokemon: Int,

    val generoPokemon: Char,
    val nivel: Int,
    val fechaCaptura: String,
    val tipoPokeball: String,
    val lifePoints: Double,
    val fuerza: Double
){

}