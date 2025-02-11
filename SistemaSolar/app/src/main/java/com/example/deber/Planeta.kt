package com.example.deber

import android.os.Parcel
import android.os.Parcelable

data class Planeta(
    val id: Int,
    val nombre: String,
    val tipoPlaneta: String,
    val distanciaAlSol: Double,
    val diametro: Double,
    val habitable: Boolean,
    val latitud: Double,
    val longitud: Double,
    val sistemaSolarId: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readByte() != 0.toByte(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeString(tipoPlaneta)
        parcel.writeDouble(distanciaAlSol)
        parcel.writeDouble(diametro)
        parcel.writeByte(if (habitable) 1 else 0)
        parcel.writeDouble(latitud)
        parcel.writeDouble(longitud)
        parcel.writeInt(sistemaSolarId)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Planeta> {
        override fun createFromParcel(parcel: Parcel): Planeta = Planeta(parcel)
        override fun newArray(size: Int): Array<Planeta?> = arrayOfNulls(size)
    }
}
