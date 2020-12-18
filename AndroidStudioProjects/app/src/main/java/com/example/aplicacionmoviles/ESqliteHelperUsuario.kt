package com.example.aplicacionmoviles

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class ESqliteHelperUsuario(
        contexto: Context?
): SQLiteOpenHelper(
        contexto,
        "moviles",null,
        1
){
    override fun onCreate(db: SQLiteDatabase?) {

        val scriptCrearTablaUsuario =
                """
                    CREATE TABLE USUARIO(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre VARCHAR(50),
                    descripcion varchar(50)
                    )
                """.trimIndent()
        Log.i("bbd","Creando la tabla del usuario")
        db?.execSQL(scriptCrearTablaUsuario)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun consultarUsuarioPorId(id:Int): EUsuarioBDD {
        val scriptConsultarUsuario = "" +
                "SELECT * FROM USUARIO " +
                "WHERE id=${id}"
        val dbReadable = readableDatabase
        val resultado = dbReadable.rawQuery(
                scriptConsultarUsuario,
                null
        )
        val existeUsuario  = resultado.moveToFirst() // iterable

        val usuarioEncontrado = EUsuarioBDD(0,"","")

        if(existeUsuario){
            do{
                val id=resultado.getInt(0)     //columna indice 0
                val nombre=resultado.getString(1)  //columna indice 1
                val descripcion=resultado.getString(2)  //columna indice 2
                    if(id!=null){
                        usuarioEncontrado.id=id
                        usuarioEncontrado.nombre=nombre
                        usuarioEncontrado.descripcion=descripcion
                    }

            }while (resultado.moveToNext())
        }
        resultado.close()
        dbReadable.close()
        return usuarioEncontrado
    }
    fun crearUsuarioFormulario(
            nombre:String,
            descripcion:String
    ):Boolean{
        //CONECION DE ESCRITURA
        val conexionDeEscritura = writableDatabase
        val valoresAGuardar = ContentValues()
        valoresAGuardar.put("nombre",nombre)
        valoresAGuardar.put("descripcion",descripcion)

        val resultadoEscritura:Long = conexionDeEscritura.
                insert(
                        "USUARIO",
                        null,
                        valoresAGuardar
                )
        conexionDeEscritura.close()

        return if (resultadoEscritura.toInt() == -1)false else true

    }

    fun actualizarUsuarioFormulario(
            nombre:String,
            descripcion: String,
            idActualizar: Int
    ):Boolean{
        val conexionEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("nombre",nombre)
        valoresAActualizar.put("descripcion",descripcion)
        val resultadoActualizacion = conexionEscritura.
                update(
                        "USUARIO",
                        valoresAActualizar,
                        "id=?",
                        arrayOf(idActualizar.toString())
                )
        conexionEscritura.close()
        return if(resultadoActualizacion.toInt()==-1) false else true
    }

    fun eliminarUsuarioFormulario(
            id: Int
    ):Boolean{
        val conexionEscritura = writableDatabase
        val resultadoEliminacion = writableDatabase.
                delete(
                        "USUARIO",
                        "id=?",
                        arrayOf(id.toString())
                )
        conexionEscritura.close()
        return if(resultadoEliminacion.toInt()==-1)false else true
    }
}