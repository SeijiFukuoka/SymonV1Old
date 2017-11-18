package br.com.symon.data.model.requests

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class UserAuthenticateRequest(
        @SerializedName("email") var email: String?,
        @SerializedName("password") var password: String?) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(email)
        writeString(password)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<UserAuthenticateRequest> = object : Parcelable.Creator<UserAuthenticateRequest> {
            override fun createFromParcel(source: Parcel): UserAuthenticateRequest = UserAuthenticateRequest(source)
            override fun newArray(size: Int): Array<UserAuthenticateRequest?> = arrayOfNulls(size)
        }
    }
}