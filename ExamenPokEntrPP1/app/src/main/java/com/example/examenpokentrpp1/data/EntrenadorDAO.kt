package com.example.examenpokentrpp1.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.examenpokentrpp1.model.Entrenador

@Dao
interface EntrenadorDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addEntrenador(entrenador: Entrenador)

    @Update
    suspend fun updateEntrenador(entrenador: Entrenador)

    @Delete
    suspend fun deleteEntrenador(entrenador: Entrenador)

    @Query("DELETE FROM entrenador_table")
    suspend fun deleteAllEntrenador()

    @Query("SELECT * FROM entrenador_table ORDER BY nombre ASC")
    fun readAllData(): LiveData<List<Entrenador>>
}