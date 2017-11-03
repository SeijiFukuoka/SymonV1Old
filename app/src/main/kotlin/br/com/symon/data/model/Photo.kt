package br.com.symon.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Photo(@SerializedName("uri") var uri: String?) : Parcelable {
  constructor(source: Parcel) : this(
      source.readString()
  )

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeString(uri)
  }

  companion object {
    @JvmField
    val CREATOR: Parcelable.Creator<Photo> = object : Parcelable.Creator<Photo> {
      override fun createFromParcel(source: Parcel): Photo = Photo(source)
      override fun newArray(size: Int): Array<Photo?> = arrayOfNulls(size)
    }
  }
}