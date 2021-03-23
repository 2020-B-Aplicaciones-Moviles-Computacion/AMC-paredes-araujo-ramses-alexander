package com.example.gamenode1.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp

class FirestorePublicacionDTO(
    var uid:String = "",
    var descripcion: String = "",
    var etiquetas:ArrayList<String> = arrayListOf(),
    var fechaPublicacion :com.google.firebase.Timestamp? = null,
    var libreria:FirestoreLibreriaPublicacionDTO? = null,
    var titulo:String = ""

): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readValue(ClassLoader.getSystemClassLoader()) as ArrayList<String>,
        parcel.readParcelable(Timestamp::class.java.classLoader),
        parcel.readValue( ClassLoader.getSystemClassLoader()) as FirestoreLibreriaPublicacionDTO?,
        parcel.readString().toString()
    ) {
    }

    override fun toString(): String {
        return ""+titulo
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(descripcion)
        parcel.writeParcelable(fechaPublicacion, flags)
        parcel.writeString(titulo)
        parcel.writeArray(arrayOf(etiquetas))
        parcel.writeValue(libreria)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FirestorePublicacionDTO> {
        override fun createFromParcel(parcel: Parcel): FirestorePublicacionDTO {
            return FirestorePublicacionDTO(parcel)
        }

        override fun newArray(size: Int): Array<FirestorePublicacionDTO?> {
            return arrayOfNulls(size)
        }
    }
}