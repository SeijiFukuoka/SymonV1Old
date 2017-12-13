package br.com.symon.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Comment(
        @SerializedName("id") var id: Int?,
        @SerializedName("message") var message: String?,
        @SerializedName("created_at") var createdAt: String?,
        @SerializedName("updated_at") var updatedAt: String?,
        @SerializedName("sale_id") var saleId: Int?,
        @SerializedName("user_id") var userId: Int?
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeString(message)
        writeString(createdAt)
        writeString(updatedAt)
        writeValue(saleId)
        writeValue(userId)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Comment> = object : Parcelable.Creator<Comment> {
            override fun createFromParcel(source: Parcel): Comment = Comment(source)
            override fun newArray(size: Int): Array<Comment?> = arrayOfNulls(size)
        }
    }
}