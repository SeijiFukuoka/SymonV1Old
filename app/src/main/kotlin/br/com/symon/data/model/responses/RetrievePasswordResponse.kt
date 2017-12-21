package br.com.symon.data.model.responses

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class RetrievePasswordResponse(
        @SerializedName("message") var message: String) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(message)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RetrievePasswordResponse> = object : Parcelable.Creator<RetrievePasswordResponse> {
            override fun createFromParcel(source: Parcel): RetrievePasswordResponse = RetrievePasswordResponse(source)
            override fun newArray(size: Int): Array<RetrievePasswordResponse?> = arrayOfNulls(size)
        }
    }
}