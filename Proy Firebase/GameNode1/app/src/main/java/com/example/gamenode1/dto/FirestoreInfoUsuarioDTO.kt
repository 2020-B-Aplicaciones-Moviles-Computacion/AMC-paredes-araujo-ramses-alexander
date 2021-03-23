package com.example.gamenode1.dto

class FirestoreInfoUsuarioDTO (
    var uid: String = "",
    var correo: String = "",
    var descripcion: String = "",
    var fechaNac: String = "",
    var fotoPerfil: String = "",
    var fotoFondo: String = "",
    var name: String = ""

){
    override fun toString(): String {
        return " NOMBRE DEL INF USUARIO, ESTO ESTA QUEMADO EN LA CLASE"
    }
}