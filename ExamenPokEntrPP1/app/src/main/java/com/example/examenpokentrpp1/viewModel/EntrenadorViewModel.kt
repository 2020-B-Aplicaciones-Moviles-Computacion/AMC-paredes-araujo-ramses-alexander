package com.example.examenpokentrpp1.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.examenpokentrpp1.data.EntrenadorDatabase
import com.example.examenpokentrpp1.repository.EntrenadorRepository
import com.example.examenpokentrpp1.model.Entrenador
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch

class EntrenadorViewModel(application: Application) : AndroidViewModel(application) {
    private val readAllData: LiveData<List<Entrenador>>
    private val repository: EntrenadorRepository

    init {
        val entrenadorDao = EntrenadorDatabase.getDatabase(application).entrenadorDao()
        repository = EntrenadorRepository(entrenadorDao)
        readAllData = repository.readAllData
    }
    fun addEntrenador(entrenador: Entrenador){
        //Debido a que queremos correr esto en un background thread
        viewModelScope.launch (Dispatchers.IO){
            repository.addEntrenador(entrenador)
        }
    }
    fun updateEntrenador(entrenador: Entrenador){
        viewModelScope.launch (Dispatchers.IO){
            repository.updateEntrenador(entrenador)
        }
    }
    fun deleteEntrenador(entrenador: Entrenador){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteEntrenador(entrenador)
        }
    }
    fun getEntrenadorList(): LiveData<List<Entrenador>> {
        return readAllData
    }
}