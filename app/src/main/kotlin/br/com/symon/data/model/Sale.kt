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
        @SerializedName("likes") var likes: Int?,
        @SerializedName("dislikes") var dislikes: Int?,
        @SerializedName("hasLiked") var hasLiked: Boolean?,
        @SerializedName("hasDisliked") var hasDisliked: Boolean?,
        @SerializedName("user") var user: User
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readValue(Float::class.java.classLoader) as Float?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
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
        writeValue(likes)
        writeValue(dislikes)
        writeValue(hasLiked)
        writeValue(hasDisliked)
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