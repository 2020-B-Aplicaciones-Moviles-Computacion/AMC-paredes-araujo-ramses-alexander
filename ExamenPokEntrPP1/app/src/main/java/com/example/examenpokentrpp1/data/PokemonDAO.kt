package com.example.examenpokentrpp1.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.examenpokentrpp1.model.Pokemon

@Dao
interface PokemonDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPokemon(pokemon: Pokemon)

    @Update
    suspend fun updatePokemon(pokemon: Pokemon)

    @Delete
    suspend fun deletePokemon(pokemon: Pokemon)

    @Query("SELECT * FROM pokemon_table ORDER BY nPokedex ASC")
    fun readAllData(): LiveData<List<Pokemon>>



    @Query("SELECT * FROM pokemon_table ")
    fun listadoPokemon(): List<Pokemon>
}
