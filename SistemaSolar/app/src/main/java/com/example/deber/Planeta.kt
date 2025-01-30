package com.example.deber

import android.os.Parcel
import android.os.Parcelable

data class Planeta(
    val id: Int,
    val nombre: String,
    val distanciaAlSol: Double,
    val tipoPlaneta: String,
    val diametro: Double,
    val sistemaSolarId: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeDouble(distanciaAlSol)
        parcel.writeString(tipoPlaneta)
        parcel.writeDouble(diametro)
        parcel.writeInt(sistemaSolarId)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Planeta> {
        override fun createFromParcel(parcel: Parcel): Planeta = Planeta(parcel)
        override fun newArray(size: Int): Array<Planeta?> = arrayOfNulls(size)
    }
}
