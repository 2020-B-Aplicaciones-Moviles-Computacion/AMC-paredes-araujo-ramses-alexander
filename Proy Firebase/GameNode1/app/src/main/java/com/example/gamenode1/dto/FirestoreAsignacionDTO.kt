package com.example.gamenode1.dto

class FirestoreAsignacionDTO(
    var uid:String ="",
    var fechaAsignacion:com.google.firebase.Timestamp? =null,
    var grupo:FirestoreGrupoAsignacionDTO?=null,
    var usuario:FirestoreUsuarioAsignacionDTO?=null

)  {
}