package br.com.symon.data.model.requests

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class UserTokenRequest(
        @SerializedName("username") var userName: String?,
        @SerializedName("password") var password: String?) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(userName)
        writeString(password)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<UserTokenRequest> = object : Parcelable.Creator<UserTokenRequest> {
            override fun createFromParcel(source: Parcel): UserTokenRequest = UserTokenRequest(source)
            override fun newArray(size: Int): Array<UserTokenRequest?> = arrayOfNulls(size)
        }
    }
}