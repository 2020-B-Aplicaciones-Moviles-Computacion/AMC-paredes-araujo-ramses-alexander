package com.example.examenpokentrpp1.repository

import androidx.lifecycle.LiveData
import com.example.examenpokentrpp1.data.CapturaDAO
import com.example.examenpokentrpp1.model.Captura
import com.example.examenpokentrpp1.model.Entrenador

class CapturaRepository(private val capturaDAO: CapturaDAO)  {
    val readAllData: LiveData<List<Captura>> = capturaDAO.readAllData()


    suspend fun addCaptura(captura: Captura){
        capturaDAO.addCaptura(captura)
    }
    suspend fun updateCaptura(captura: Captura){
        capturaDAO.updateCaptura(captura)
    }
    suspend fun deleteCaptura(captura: Captura){
        capturaDAO.deleteCaptura(captura)
    }
    fun readAllCapturasEntrenadorFunc(idBaseEntrenador: Int):  LiveData<List<Captura>>{
        return capturaDAO.readAllDataEntrenador(idBaseEntrenador)
    }
    suspend fun deleteAllCapturasEntrenador(idBaseEntrenador: Int){
        capturaDAO.deleteAllCapturasEntrenador(idBaseEntrenador)
    }
    suspend fun deleteAllCapturasPokemon(idPokemon: Int){
        capturaDAO.deleteAllCapturasPokemon(idPokemon)
    }
}