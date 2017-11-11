package br.com.symon.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.*

data class User(
        @SerializedName("id") var id: Int?,
        @SerializedName("name") var name: String?,
        @SerializedName("email") var email: String?,
        @SerializedName("phone") var phone: String?,
        @SerializedName("birthday") var birthday: Date?,
        @SerializedName("facebookId") var facebookId: String?,
        @SerializedName("photo") var photo: String?) : Parcelable {

    constructor() : this (id = null, name = null, email = null, phone = null, birthday = null, facebookId = null, photo = null)

    constructor(source: Parcel) : this(
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readSerializable() as Date?,
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeString(name)
        writeString(email)
        writeString(phone)
        writeSerializable(birthday)
        writeString(facebookId)
        writeString(photo)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }
}


