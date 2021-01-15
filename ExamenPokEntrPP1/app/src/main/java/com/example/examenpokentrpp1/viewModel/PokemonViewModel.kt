package com.example.examenpokentrpp1.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.examenpokentrpp1.data.EntrenadorDatabase
import com.example.examenpokentrpp1.repository.PokemonRepository
import com.example.examenpokentrpp1.model.Pokemon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PokemonViewModel (application: Application) : AndroidViewModel(application) {
    private val readAllData: LiveData<List<Pokemon>>
    //private val listadoPokemon: List<Pokemon>
    private val repository: PokemonRepository

    init {
        val pokemonDAO = EntrenadorDatabase.getDatabase(application).pokemonDao()
        repository = PokemonRepository(pokemonDAO)
        readAllData = repository.readAllData
    }
    fun addPokemon(pokemon: Pokemon){
        //Debido a que queremos correr esto en un background thread
        viewModelScope.launch (Dispatchers.IO){
            repository.addPokemon(pokemon)
        }
    }
    fun updatePokemon(pokemon: Pokemon){
        viewModelScope.launch (Dispatchers.IO){
            repository.updatePokemon(pokemon)
        }
    }
    fun deletePokemon(pokemon: Pokemon){
        viewModelScope.launch (Dispatchers.IO){
            repository.deletePokemon(pokemon)
        }
    }
    fun getPokemonList(): LiveData<List<Pokemon>> {
        return readAllData
    }
    fun getListadoPokemon():MutableLiveData<List<Pokemon>>{
        val a = MutableLiveData<List<Pokemon>>()
        viewModelScope.launch {
            val add = repository.getListadoPokemon()
            a.postValue(add)
        }
        return a
    }
}
