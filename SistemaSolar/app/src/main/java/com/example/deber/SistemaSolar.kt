package com.example.deber

import android.os.Parcel
import android.os.Parcelable

data class SistemaSolar(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val edad: Int,
    val tipoEstrella: String,
    val esHabitable: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeString(descripcion)
        parcel.writeInt(edad)
        parcel.writeString(tipoEstrella)
        parcel.writeByte(if (esHabitable) 1 else 0)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<SistemaSolar> {
        override fun createFromParcel(parcel: Parcel): SistemaSolar = SistemaSolar(parcel)
        override fun newArray(size: Int): Array<SistemaSolar?> = arrayOfNulls(size)
    }
}
