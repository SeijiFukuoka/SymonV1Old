package br.com.symon.data.model.responses

import android.os.Parcel
import android.os.Parcelable
import br.com.symon.data.model.User
import com.google.gson.annotations.SerializedName

data class SendSaleCommentResponse(
        @SerializedName("id") var id: Int,
        @SerializedName("message") var message: String,
        @SerializedName("sale_id") var saleId: Int,
        @SerializedName("formatted_date") var formattedDate: String,
        @SerializedName("user") var user: User) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readInt(),
            source.readString(),
            source.readParcelable<User>(User::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(message)
        writeInt(saleId)
        writeString(formattedDate)
        writeParcelable(user, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SendSaleCommentResponse> = object : Parcelable.Creator<SendSaleCommentResponse> {
            override fun createFromParcel(source: Parcel): SendSaleCommentResponse = SendSaleCommentResponse(source)
            override fun newArray(size: Int): Array<SendSaleCommentResponse?> = arrayOfNulls(size)
        }
    }
}