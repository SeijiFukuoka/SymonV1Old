package br.com.symon.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class BlockedUser(
        @SerializedName("id") var id: Int,
        @SerializedName("created_at") var createdAt: String,
        @SerializedName("updated_at") var updatedAt: String,
        @SerializedName("blocker_id") var blockerId: Int,
        @SerializedName("blocked_id") var blockedId: Int,
        @SerializedName("formatted_date") var formattedDate: String,
        @SerializedName("user") var user: User) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readInt(),
            source.readString(),
            source.readParcelable<User>(User::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(createdAt)
        writeString(updatedAt)
        writeInt(blockerId)
        writeInt(blockedId)
        writeString(formattedDate)
        writeParcelable(user, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<BlockedUser> = object : Parcelable.Creator<BlockedUser> {
            override fun createFromParcel(source: Parcel): BlockedUser = BlockedUser(source)
            override fun newArray(size: Int): Array<BlockedUser?> = arrayOfNulls(size)
        }
    }
}


