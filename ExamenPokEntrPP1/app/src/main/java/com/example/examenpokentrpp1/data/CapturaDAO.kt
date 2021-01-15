package com.example.examenpokentrpp1.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.examenpokentrpp1.model.Captura

@Dao
interface CapturaDAO {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCaptura(captura: Captura)

    @Update
    suspend fun updateCaptura(captura: Captura)

    @Delete
    suspend fun deleteCaptura(captura: Captura)

    //Para borrar todas las capturas de un entrenador
    @Query("DELETE FROM captura_table WHERE idBaseEntrenador= :idEntrenBorrar ")
    suspend fun deleteAllCapturasEntrenador(idEntrenBorrar:Int)

    //Para borrar todas las capturas de un pokemon
    @Query("DELETE FROM captura_table WHERE idBasePokemon= :idBasePokemon ")
    suspend fun deleteAllCapturasPokemon(idBasePokemon:Int)


    @Query("SELECT * FROM captura_table ORDER BY idCaptura ASC")
    fun readAllData(): LiveData<List<Captura>>

    @Query("SELECT * FROM captura_table WHERE idBaseEntrenador LIKE :idBaseEntr  ORDER BY idCaptura ASC")
    fun readAllDataEntrenador(idBaseEntr:Int): LiveData<List<Captura>>

}