package com.example.gamenode1.dto

class FirestorePublicacionComentarioDTO (
        var uid: String = ""

){
    override fun toString(): String {
        return " ${uid}"
    }
}