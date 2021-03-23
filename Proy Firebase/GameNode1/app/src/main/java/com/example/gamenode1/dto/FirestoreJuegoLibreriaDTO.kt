package com.example.gamenode1.dto

class FirestoreJuegoLibreriaDTO (
        var uid: String = "",
        var desarrolladora: String = "",
        var fotoJuego: String = "",
        var nombre: String = "",
        var tipo: String = ""

) {
    override fun toString(): String {
        return ""+nombre
    }

}