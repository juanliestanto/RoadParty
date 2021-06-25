package com.example.roadparty.Database

import android.os.Parcel
import android.os.Parcelable


data class JenisPakaian (
        val jenis: String?,
        val harga: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(jenis)
        parcel.writeString(harga)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<JenisPakaian> {
        override fun createFromParcel(parcel: Parcel): JenisPakaian {
            return JenisPakaian(parcel)
        }

        override fun newArray(size: Int): Array<JenisPakaian?> {
            return arrayOfNulls(size)
        }
    }
}