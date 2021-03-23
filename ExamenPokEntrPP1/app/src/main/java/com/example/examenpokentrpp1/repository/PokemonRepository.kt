package com.example.examenpokentrpp1.repository

import androidx.lifecycle.LiveData
import com.example.examenpokentrpp1.data.PokemonDAO
import com.example.examenpokentrpp1.model.Pokemon

class PokemonRepository(private val pokemonDAO: PokemonDAO) {
    val readAllData: LiveData<List<Pokemon>> = pokemonDAO.readAllData()

    suspend fun addPokemon(pokemon: Pokemon){
        pokemonDAO.addPokemon(pokemon)
    }

    suspend fun updatePokemon(pokemon: Pokemon){
        pokemonDAO.updatePokemon(pokemon)
    }

    suspend fun deletePokemon(pokemon: Pokemon){
        pokemonDAO.deletePokemon(pokemon)
    }
    suspend fun getListadoPokemon():List<Pokemon>{
        return pokemonDAO.listadoPokemon()
    }

}
