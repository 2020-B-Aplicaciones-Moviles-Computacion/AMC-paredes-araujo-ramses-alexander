package com.example.examenpokentrpp1.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entrenador_table")
class Entrenador (
        @PrimaryKey(autoGenerate = true)
        val idBaseEntrenador: Int,
        val idEntrenador: String,
        val nombre: String?,
        val genero: Char?,
        val fechaNac: String?,
        val activo: Boolean?
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readValue(Char::class.java.classLoader) as? Char,
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idBaseEntrenador)
        parcel.writeString(idEntrenador)
        parcel.writeString(nombre)
        parcel.writeValue(genero)
        parcel.writeString(fechaNac)
        parcel.writeValue(activo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Entrenador> {
        override fun createFromParcel(parcel: Parcel): Entrenador {
            return Entrenador(parcel)
        }

        override fun newArray(size: Int): Array<Entrenador?> {
            return arrayOfNulls(size)
        }
    }

}