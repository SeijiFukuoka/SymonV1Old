package br.com.symon.data.model.requests

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal


data class SendSaleRequest(@SerializedName("message") val productName: String,
                           @SerializedName("place") val placeName: String,
                           @SerializedName("price") val price: BigDecimal,
                           @SerializedName("latitude") val lat: Double,
                           @SerializedName("longitude") val lng: Double) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readSerializable() as BigDecimal,
            source.readDouble(),
            source.readDouble()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(productName)
        writeString(placeName)
        writeSerializable(price)
        writeDouble(lat)
        writeDouble(lng)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SendSaleRequest> = object : Parcelable.Creator<SendSaleRequest> {
            override fun createFromParcel(source: Parcel): SendSaleRequest = SendSaleRequest(source)
            override fun newArray(size: Int): Array<SendSaleRequest?> = arrayOfNulls(size)
        }
    }
}