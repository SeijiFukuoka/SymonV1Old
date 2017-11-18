package br.com.symon.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Sale(
        @SerializedName("id") var id: Int?,
        @SerializedName("message") var message: String?,
        @SerializedName("value") var value: String?,
        @SerializedName("created_at") private var createdAt: String?,
        @SerializedName("updated_at") var updatedAt: String?,
        @SerializedName("photo") var photo: Photo?,
        @SerializedName("isSponsored") var isSponsored: Boolean?,
        @SerializedName("isLiked") var isLiked: Boolean?,
        @SerializedName("isDisliked") var isDisliked: Boolean?
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readParcelable<Photo>(Photo::class.java.classLoader),
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeString(message)
        writeString(value)
        writeString(createdAt)
        writeString(updatedAt)
        writeParcelable(photo, 0)
        writeValue(isSponsored)
        writeValue(isLiked)
        writeValue(isDisliked)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Sale> = object : Parcelable.Creator<Sale> {
            override fun createFromParcel(source: Parcel): Sale = Sale(source)
            override fun newArray(size: Int): Array<Sale?> = arrayOfNulls(size)
        }
    }
}