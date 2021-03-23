package com.example.examenpokentrpp1.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*

@Entity(tableName = "pokemon_table")
class Pokemon(
    @PrimaryKey(autoGenerate = true)
    val idPokemon: Int,
    val nPokedex: Int,
    val nombre: String,
    val tipo: Int,
    val altura: Double,
    val peso: Double
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idPokemon)
        parcel.writeInt(nPokedex)
        parcel.writeString(nombre)
        parcel.writeInt(tipo)
        parcel.writeDouble(altura)
        parcel.writeDouble(peso)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Pokemon> {
        override fun createFromParcel(parcel: Parcel): Pokemon {
            return Pokemon(parcel)
        }

        override fun newArray(size: Int): Array<Pokemon?> {
            return arrayOfNulls(size)
        }
    }

}
