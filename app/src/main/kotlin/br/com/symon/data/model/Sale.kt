package br.com.symon.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Sale(
        @SerializedName("id") var id: Int?,
        @SerializedName("message") var message: String?,
        @SerializedName("price") var price: Float?,
        @SerializedName("created_at") private var createdAt: String?,
        @SerializedName("updated_at") var updatedAt: String?,
        @SerializedName("photo") var photo: String?,
        @SerializedName("likes") var likes: String?,
        @SerializedName("dislikes") var dislikes: String?,
        @SerializedName("user") var user: User
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readValue(Float::class.java.classLoader) as Float?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readParcelable<User>(User::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeString(message)
        writeValue(price)
        writeString(createdAt)
        writeString(updatedAt)
        writeString(photo)
        writeString(likes)
        writeString(dislikes)
        writeParcelable(user, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Sale> = object : Parcelable.Creator<Sale> {
            override fun createFromParcel(source: Parcel): Sale = Sale(source)
            override fun newArray(size: Int): Array<Sale?> = arrayOfNulls(size)
        }
    }
}