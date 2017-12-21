package br.com.symon.data.model

import android.os.Parcel
import android.os.Parcelable

data class Settings(val itsCheapPushNotificationEnable: Boolean = true,
                    val itsExpensivePushNotificationEnable: Boolean = true,
                    val commentsPushNotificationEnable: Boolean = true) : Parcelable {
    constructor(source: Parcel) : this(
            1 == source.readInt(),
            1 == source.readInt(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt((if (itsCheapPushNotificationEnable) 1 else 0))
        writeInt((if (itsExpensivePushNotificationEnable) 1 else 0))
        writeInt((if (commentsPushNotificationEnable) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Settings> = object : Parcelable.Creator<Settings> {
            override fun createFromParcel(source: Parcel): Settings = Settings(source)
            override fun newArray(size: Int): Array<Settings?> = arrayOfNulls(size)
        }
    }
}