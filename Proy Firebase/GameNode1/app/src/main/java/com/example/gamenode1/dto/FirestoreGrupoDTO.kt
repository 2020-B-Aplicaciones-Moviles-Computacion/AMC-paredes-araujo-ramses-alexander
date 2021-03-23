package com.example.gamenode1.dto

class FirestoreGrupoDTO (
    var uid:String = "",
    var usuario:FirestoreUsuarioGrupoDTO? = null,
    var nombre:String = "",
    var descripcion:String = "",
    var imagen:String = "",
    var juego:FirestoreJuegoGrupoDTO? = null,
    var fechaCreacion:com.google.firebase.Timestamp? = null
){}