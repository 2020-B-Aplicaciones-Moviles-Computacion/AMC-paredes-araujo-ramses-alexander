package com.example.gamenode1

import com.example.gamenode1.dto.FirestoreInfoUsuarioDTO
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class AuthUsuario {

    companion object{
        var usuario: UsuarioFirebase?
        var infUsuario: FirestoreInfoUsuarioDTO?
        var locacion: LatLng?
        var storage = Firebase.storage
        init {
            this.usuario=null;
            this.infUsuario=null;
            this.locacion=null;
        }
    }
}