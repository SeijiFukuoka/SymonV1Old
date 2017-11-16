package br.com.symon.data.model

import android.os.Parcel
import android.os.Parcelable

data class Settings(var settingsList: MutableList<NotificationStatus>?) : Parcelable {
    constructor(source: Parcel) : this(
            source.createTypedArrayList(NotificationStatus.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeTypedList(settingsList)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Settings> = object : Parcelable.Creator<Settings> {
            override fun createFromParcel(source: Parcel): Settings = Settings(source)
            override fun newArray(size: Int): Array<Settings?> = arrayOfNulls(size)
        }
    }
}