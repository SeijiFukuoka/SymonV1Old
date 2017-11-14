package br.com.symon.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class UserTokenResponse(
        @SerializedName("user") var user: User?,
        @SerializedName("token") var token: String) : Parcelable {
    constructor(source: Parcel) : this(
            source.readParcelable<User>(User::class.java.classLoader),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(user, 0)
        writeString(token)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<UserTokenResponse> = object : Parcelable.Creator<UserTokenResponse> {
            override fun createFromParcel(source: Parcel): UserTokenResponse = UserTokenResponse(source)
            override fun newArray(size: Int): Array<UserTokenResponse?> = arrayOfNulls(size)
        }
    }
}