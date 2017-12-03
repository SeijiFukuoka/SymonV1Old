package br.com.symon.data.model.requests

import com.google.gson.annotations.SerializedName
import java.util.*

data class UserUpdateRequest(@SerializedName("name") var name: String?,
                             @SerializedName("phone") var phone: String?,
                             @SerializedName("birthday") var birthday: Date?,
                             @SerializedName("currentPassword") var currentPassword: String? = null,
                             @SerializedName("newPassword") val newPassword: String? = null)