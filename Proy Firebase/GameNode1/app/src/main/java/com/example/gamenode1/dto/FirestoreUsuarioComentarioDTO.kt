package com.example.gamenode1.dto

class FirestoreUsuarioComentarioDTO (
        var uid: String = "",
        var foto: String = "",
        var nombre: String = ""

){
    override fun toString(): String {
        return " user: ${nombre}"
    }
}