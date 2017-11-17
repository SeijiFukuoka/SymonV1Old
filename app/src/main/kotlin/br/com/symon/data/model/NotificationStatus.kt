package br.com.symon.data.model

import android.os.Parcel
import android.os.Parcelable

data class NotificationStatus(
        var type: Int?,
        var name: String?,
        var isActive: Boolean
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(type)
        writeString(name)
        writeInt((if (isActive) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<NotificationStatus> = object : Parcelable.Creator<NotificationStatus> {
            override fun createFromParcel(source: Parcel): NotificationStatus = NotificationStatus(source)
            override fun newArray(size: Int): Array<NotificationStatus?> = arrayOfNulls(size)
        }
    }
}