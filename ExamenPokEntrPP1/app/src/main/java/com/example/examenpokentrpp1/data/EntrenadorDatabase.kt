package com.example.examenpokentrpp1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.examenpokentrpp1.model.Captura
import com.example.examenpokentrpp1.model.Pokemon
import com.example.examenpokentrpp1.model.Entrenador

@Database(entities = [Entrenador::class, Pokemon::class, Captura::class],version = 1, exportSchema = false )
abstract class EntrenadorDatabase:RoomDatabase() {

    abstract fun entrenadorDao():EntrenadorDAO
    abstract fun pokemonDao(): PokemonDAO
    abstract fun capturaDao(): CapturaDAO

    companion object{
        @Volatile
        private var INSTANCE: EntrenadorDatabase? = null
        fun getDatabase(context: Context):EntrenadorDatabase{
            val tempinstance = INSTANCE
            if(tempinstance!=null){
                return tempinstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EntrenadorDatabase::class.java,
                    "entrenador_database"
                ).build()
                INSTANCE = instance
                return instance

            }
        }
    }
}