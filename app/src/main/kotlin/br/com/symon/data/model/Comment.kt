package br.com.symon.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Comment(
        @SerializedName("id") var id: Int,
        @SerializedName("message") var message: String,
        @SerializedName("created_at") var createdAt: String?,
        @SerializedName("updated_at") var updatedAt: String?,
        @SerializedName("sale_id") var saleId: Int,
        @SerializedName("user_id") var userId: Int
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(message)
        writeString(createdAt)
        writeString(updatedAt)
        writeInt(saleId)
        writeInt(userId)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Comment> = object : Parcelable.Creator<Comment> {
            override fun createFromParcel(source: Parcel): Comment = Comment(source)
            override fun newArray(size: Int): Array<Comment?> = arrayOfNulls(size)
        }
    }
}