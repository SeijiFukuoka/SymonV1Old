package br.com.symon.data.model.responses

import android.os.Parcel
import android.os.Parcelable
import br.com.symon.data.model.Sale
import com.google.gson.annotations.SerializedName

data class SalesListResponse(
        @SerializedName("items") var salesList: MutableList<Sale>,
        @SerializedName("totalPages") var totalPages: Int,
        @SerializedName("totalItems") var totalItems: Int
) : Parcelable {
    constructor(source: Parcel) : this(
            source.createTypedArrayList(Sale.CREATOR),
            source.readInt(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeTypedList(salesList)
        writeInt(totalPages)
        writeInt(totalItems)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SalesListResponse> = object : Parcelable.Creator<SalesListResponse> {
            override fun createFromParcel(source: Parcel): SalesListResponse = SalesListResponse(source)
            override fun newArray(size: Int): Array<SalesListResponse?> = arrayOfNulls(size)
        }
    }
}