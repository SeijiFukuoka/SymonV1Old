package br.com.symon.data.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") var id: Int?,
    @SerializedName("name") var name: String?,
    @SerializedName("email") var email: String?,
    @SerializedName("username") var username: String?,
    @SerializedName("password") var password: String?,
    @SerializedName("created_at") var createdAt: String?,
    @SerializedName("updated_at") var updatedAt: String?,
    @SerializedName("reset_password_token") var resetPasswordToken: String?,
    @SerializedName("reset_password_expires") var resetPasswordExpires: String?,
    @SerializedName("role_id") var roleId: Int?,
    @SerializedName("photo_id") var photoId: Int?) : Parcelable {

  constructor(parcel: Parcel) : this(
      parcel.readValue(Int::class.java.classLoader) as? Int,
      parcel.readString(),
      parcel.readString(),
      parcel.readString(),
      parcel.readString(),
      parcel.readString(),
      parcel.readString(),
      parcel.readString(),
      parcel.readString(),
      parcel.readValue(Int::class.java.classLoader) as? Int,
      parcel.readValue(Int::class.java.classLoader) as? Int)

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeValue(id)
    parcel.writeString(name)
    parcel.writeString(email)
    parcel.writeString(username)
    parcel.writeString(password)
    parcel.writeString(createdAt)
    parcel.writeString(updatedAt)
    parcel.writeString(resetPasswordToken)
    parcel.writeString(resetPasswordExpires)
    parcel.writeValue(roleId)
    parcel.writeValue(photoId)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Creator<User> {
    override fun createFromParcel(parcel: Parcel): User {
      return User(parcel)
    }

    override fun newArray(size: Int): Array<User?> {
      return arrayOfNulls(size)
    }
  }
}


