package com.example.gamenode1.dto

import java.security.Timestamp

class FirestoreLibreriaDTO(
        var uid:String = "",
        var fechaAgregacion : com.google.firebase.Timestamp? = null,
        var usuario: FirestoreUsuarioLibreriaDTO? = null,
        var juego: FirestoreJuegoLibreriaDTO? = null
) {
    override fun toString(): String {
        return " ${juego?.nombre} "
    }
}