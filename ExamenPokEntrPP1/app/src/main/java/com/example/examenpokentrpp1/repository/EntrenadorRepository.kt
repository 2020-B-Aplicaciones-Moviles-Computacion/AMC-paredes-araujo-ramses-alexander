package com.example.examenpokentrpp1.repository

import androidx.lifecycle.LiveData
import com.example.examenpokentrpp1.data.EntrenadorDAO
import com.example.examenpokentrpp1.model.Entrenador

class EntrenadorRepository(private val entrenadorDAO: EntrenadorDAO) {
    val readAllData: LiveData<List<Entrenador>> = entrenadorDAO.readAllData()

    suspend fun addEntrenador(entrenador: Entrenador){
        entrenadorDAO.addEntrenador(entrenador)
    }
    suspend fun updateEntrenador(entrenador: Entrenador){
        entrenadorDAO.updateEntrenador(entrenador)
    }
    suspend fun deleteEntrenador(entrenador: Entrenador){
        entrenadorDAO.deleteEntrenador(entrenador)
    }
}