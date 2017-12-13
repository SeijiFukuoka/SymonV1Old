package br.com.symon.data.model

import android.os.Parcel
import android.os.Parcelable


class PlaceInfo(val id: String?, val name: String, val address: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(address)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlaceInfo> {
        override fun createFromParcel(parcel: Parcel): PlaceInfo {
            return PlaceInfo(parcel)
        }

        override fun newArray(size: Int): Array<PlaceInfo?> {
            return arrayOfNulls(size)
        }
    }
}