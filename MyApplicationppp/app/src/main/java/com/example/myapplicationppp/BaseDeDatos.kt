package com.example.myapplicationppp

import android.content.Context
import android.util.Log
import androidx.room.*

class BaseDeDatos {
    fun crearBaseDeDatos( context:Context){
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-prueba"


        ).build()
        Log.i("ciclo-vida","se creo la base")
    }
}






//-----------------------------------------------


@Entity
data class Playlist(
    @PrimaryKey val playlistId: Long,
    val playlistName: String
)

@Entity
data class Song(
    @PrimaryKey val songId: Long,
    val songName: String,
    val artist: String
)

@Entity(primaryKeys = ["playlistId", "songId"])
data class PlaylistSongCrossRef(
    val playlistId: Long,
    val songId: Long
)