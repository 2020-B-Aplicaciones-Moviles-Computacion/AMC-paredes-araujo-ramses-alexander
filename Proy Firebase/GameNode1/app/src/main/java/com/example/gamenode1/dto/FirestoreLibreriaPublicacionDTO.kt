package com.example.gamenode1.dto

import android.os.Parcel
import android.os.Parcelable

class FirestoreLibreriaPublicacionDTO(
        var uid:String = "",
        var uidJuego: String = "",
        var fotoJuego: String = "",
        var nombreJuego: String = "",
        var uidUsuario: String = "",
        var nombreUsuario: String = "",
        var fotoUsuario: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(uidJuego)
        parcel.writeString(fotoJuego)
        parcel.writeString(nombreJuego)
        parcel.writeString(uidUsuario)
        parcel.writeString(nombreUsuario)
        parcel.writeString(fotoUsuario)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FirestoreLibreriaPublicacionDTO> {
        override fun createFromParcel(parcel: Parcel): FirestoreLibreriaPublicacionDTO {
            return FirestoreLibreriaPublicacionDTO(parcel)
        }

        override fun newArray(size: Int): Array<FirestoreLibreriaPublicacionDTO?> {
            return arrayOfNulls(size)
        }
    }
}