package br.com.symon.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class UserLikeResponse(
        @SerializedName("id") var id: Int?,
        @SerializedName("liked") var liked: Boolean?,
        @SerializedName("activated") var activated: Boolean?,
        @SerializedName("created_at") private var createdAt: String?,
        @SerializedName("updated_at") var updatedAt: String?,
        @SerializedName("sale_id") var saleId: Int?,
        @SerializedName("user_id") var userId: Int?,
        @SerializedName("user") var user: User
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readString(),
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readParcelable<User>(User::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeValue(liked)
        writeValue(activated)
        writeString(createdAt)
        writeString(updatedAt)
        writeValue(saleId)
        writeValue(userId)
        writeParcelable(user, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<UserLikeResponse> = object : Parcelable.Creator<UserLikeResponse> {
            override fun createFromParcel(source: Parcel): UserLikeResponse = UserLikeResponse(source)
            override fun newArray(size: Int): Array<UserLikeResponse?> = arrayOfNulls(size)
        }
    }
}