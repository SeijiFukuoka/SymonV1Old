package br.com.symon.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Sale(
    @SerializedName("id") var id: Int?,
    @SerializedName("message") var message: String?,
    @SerializedName("created_at") var createdAt: String?,
    @SerializedName("updated_at") var updatedAt: String?,
    @SerializedName("photo") var photo: Photo?
) : Parcelable {
  constructor(source: Parcel) : this(
      source.readValue(Int::class.java.classLoader) as Int?,
      source.readString(),
      source.readString(),
      source.readString(),
      source.readParcelable<Photo>(Photo::class.java.classLoader)
  )

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeValue(id)
    writeString(message)
    writeString(createdAt)
    writeString(updatedAt)
    writeParcelable(photo, 0)
  }

  companion object {
    @JvmField
    val CREATOR: Parcelable.Creator<Sale> = object : Parcelable.Creator<Sale> {
      override fun createFromParcel(source: Parcel): Sale = Sale(source)
      override fun newArray(size: Int): Array<Sale?> = arrayOfNulls(size)
    }
  }
}