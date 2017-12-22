package br.com.symon.data.model.requests

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class BlockUserRequest(@SerializedName("blocked_id") var userBlockedId: Int) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(userBlockedId)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<BlockUserRequest> = object : Parcelable.Creator<BlockUserRequest> {
            override fun createFromParcel(source: Parcel): BlockUserRequest = BlockUserRequest(source)
            override fun newArray(size: Int): Array<BlockUserRequest?> = arrayOfNulls(size)
        }
    }
}