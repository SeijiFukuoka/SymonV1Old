package br.com.symon.data.model.responses

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class SendSaleCommentResponse(
        @SerializedName("id") var id: Int,
        @SerializedName("message") var message: String,
        @SerializedName("sale_id") var saleId: String,
        @SerializedName("user_id") var userId: Int) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(message)
        writeString(saleId)
        writeInt(userId)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SendSaleCommentResponse> = object : Parcelable.Creator<SendSaleCommentResponse> {
            override fun createFromParcel(source: Parcel): SendSaleCommentResponse = SendSaleCommentResponse(source)
            override fun newArray(size: Int): Array<SendSaleCommentResponse?> = arrayOfNulls(size)
        }
    }
}