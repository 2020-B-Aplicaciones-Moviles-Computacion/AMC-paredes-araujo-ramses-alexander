package com.example.aplicacionmoviles

class BEntrenador(
       var nombre:String,
       var  descripcion:String
    ){
    override fun toString(): String {
        return "Nombre: ${this.nombre} Descripcion: ${descripcion}"
    }
}
