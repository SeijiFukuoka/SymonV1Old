package br.com.symon.data.model.responses

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class RegisterUserResponse(@SerializedName("id") val id: Int,
                                @SerializedName("uri") val photo: String) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(photo)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RegisterUserResponse> = object : Parcelable.Creator<RegisterUserResponse> {
            override fun createFromParcel(source: Parcel): RegisterUserResponse = RegisterUserResponse(source)
            override fun newArray(size: Int): Array<RegisterUserResponse?> = arrayOfNulls(size)
        }
    }
}