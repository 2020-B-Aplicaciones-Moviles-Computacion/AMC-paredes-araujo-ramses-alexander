package com.example.gamenode1.dto

class FirestorePublicacionGrupoDTO(
        var uid:String = "",
        var titulo:String ="",
        var descripcion:String="",
        var asignacion:FirestorePublicacionAsignacionDTO?=null,
        val fechaAgregacionPublicacion:com.google.firebase.Timestamp? = null
) {
}