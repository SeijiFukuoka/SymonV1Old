package br.com.symon.data.model.requests

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class SendSaleCommentRequest(
        @SerializedName("message") var commentMessage: String,
        @SerializedName("sale_id") var saleId: String) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(commentMessage)
        writeString(saleId)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SendSaleCommentRequest> = object : Parcelable.Creator<SendSaleCommentRequest> {
            override fun createFromParcel(source: Parcel): SendSaleCommentRequest = SendSaleCommentRequest(source)
            override fun newArray(size: Int): Array<SendSaleCommentRequest?> = arrayOfNulls(size)
        }
    }
}