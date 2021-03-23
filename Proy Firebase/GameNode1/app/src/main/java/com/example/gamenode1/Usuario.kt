package com.example.gamenode1

import android.graphics.Bitmap

class Usuario (
        var uid: String = "",
        var correo: String = "",
        var descripcion: String = "",
        var fechaNac: String = "",
        var fotoPerfil: Bitmap? = null,
        var fotoFondo: Bitmap? = null
){
}