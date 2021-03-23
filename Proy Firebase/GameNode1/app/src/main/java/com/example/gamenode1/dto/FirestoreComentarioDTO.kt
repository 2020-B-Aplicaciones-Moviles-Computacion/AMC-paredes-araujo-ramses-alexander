package com.example.gamenode1.dto

class FirestoreComentarioDTO (
        //usuarioComentario
        //publicacionComentario
        var uid: String = "",
        var descripcion: String = "",
        var fechaAgregacionComentario :com.google.firebase.Timestamp? = null,
        var publicacion: FirestorePublicacionComentarioDTO? = null,
        var usuario: FirestoreUsuarioComentarioDTO? = null


        ){
    override fun toString(): String {
        return " ${descripcion}"
    }
}