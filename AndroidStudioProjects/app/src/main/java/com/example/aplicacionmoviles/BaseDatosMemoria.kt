package com.example.aplicacionmoviles

class BaseDatosMemoria {
    companion object{
        val arregloEnteros = arrayListOf<Int>()
        fun cargaInicialDatos(){
            arregloEnteros.add(1)
            arregloEnteros.add(2)
            arregloEnteros.add(3)
        }
        val arregloEntrenadores = arrayListOf<BEntrenador>()
        fun cagarEntrenadores(){
            arregloEntrenadores.add(BEntrenador("UNO","DES",null))
            arregloEntrenadores.add(BEntrenador("DOS","DES2",null))
        }
    }

}