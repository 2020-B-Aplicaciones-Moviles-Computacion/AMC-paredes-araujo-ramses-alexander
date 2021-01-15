package com.example.myapplicationppp

import android.content.Context
import android.util.Log
import androidx.room.Room
import java.io.IOException

class BasesDeDatos {

    private lateinit var userDao: UserDao
    private lateinit var db: AppDatabase

    fun crearBaseDeDatos( context: Context){
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-prueba"


        ).build()
        Log.i("ciclo-vida","se creo la base")
    }


    //@Before
    fun createDb(context: Context) {
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).build()
        userDao = db.userDao()
    }

    //@After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    //@Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        val user: User = User(1,",","")
        userDao.insertAll(user)
        val byName = userDao.findByName("george","")
        Log.i("base","valiooo")
    }


}