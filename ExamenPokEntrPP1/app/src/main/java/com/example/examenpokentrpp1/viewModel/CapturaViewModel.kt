package com.example.examenpokentrpp1.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.examenpokentrpp1.data.EntrenadorDatabase
import com.example.examenpokentrpp1.model.Captura
import com.example.examenpokentrpp1.repository.CapturaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CapturaViewModel (application: Application) : AndroidViewModel(application)  {
    private val readAllData: LiveData<List<Captura>>
    private val repository: CapturaRepository

    init {
        val capturaDAO = EntrenadorDatabase.getDatabase(application).capturaDao()
        repository = CapturaRepository(capturaDAO)
        readAllData = repository.readAllData
    }
    fun addCaptura(captura: Captura){
        //Debido a que queremos correr esto en un background thread
        viewModelScope.launch (Dispatchers.IO){
            repository.addCaptura(captura)
        }
    }
    fun updateCaptura(captura: Captura){
        viewModelScope.launch (Dispatchers.IO){
            repository.updateCaptura(captura)
        }
    }
    fun deleteCaptura(captura: Captura){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteCaptura(captura)
        }
    }
    fun deleteCapturasEntrenador(idBaseEntrenador:Int){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteAllCapturasEntrenador(idBaseEntrenador)
        }
    }

    fun deleteCapturasPokemon(idPokemon:Int){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteAllCapturasPokemon(idPokemon)
        }
    }

    fun getCapturaList(): LiveData<List<Captura>> {
        return readAllData
    }

}