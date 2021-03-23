package com.example.gamenode1.dto

class FirestoreJuegoDTO(
    var uid: String = "",
    var desarrolladora: String = "",
    var descripcion: String = "",
    var fechaSalida: String = "",
    var fotoFondo: String = "",
    var nombre: String = "",
    var tipo: String = "",
    var usuarioJuegoDTO: FirestoreUsuarioJuegoDTO?=null

) {
    override fun toString(): String {
        return ""+nombre
    }

}