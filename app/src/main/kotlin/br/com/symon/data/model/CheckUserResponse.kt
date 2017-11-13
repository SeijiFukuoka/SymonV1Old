package br.com.symon.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CheckUserResponse(
        @SerializedName("exists") var exists: Boolean?) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Boolean::class.java.classLoader) as Boolean?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(exists)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CheckUserResponse> = object : Parcelable.Creator<CheckUserResponse> {
            override fun createFromParcel(source: Parcel): CheckUserResponse = CheckUserResponse(source)
            override fun newArray(size: Int): Array<CheckUserResponse?> = arrayOfNulls(size)
        }
    }
}