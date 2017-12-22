package br.com.symon.data.model.requests

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class RetrievePasswordRequest(
        @SerializedName("email") var email: String) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(email)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RetrievePasswordRequest> = object : Parcelable.Creator<RetrievePasswordRequest> {
            override fun createFromParcel(source: Parcel): RetrievePasswordRequest = RetrievePasswordRequest(source)
            override fun newArray(size: Int): Array<RetrievePasswordRequest?> = arrayOfNulls(size)
        }
    }
}