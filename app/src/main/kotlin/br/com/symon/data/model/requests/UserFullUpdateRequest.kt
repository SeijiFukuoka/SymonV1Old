package br.com.symon.data.model.requests

import com.google.gson.annotations.SerializedName
import java.util.*


class UserFullUpdateRequest(@SerializedName("name") var name: String,
                            @SerializedName("phone") var phone: String,
                            @SerializedName("birthday") var birthday: Date,
                            @SerializedName("email") var email: String?,
                            @SerializedName("password") var currentPassword: String? = null,
                            @SerializedName("new_password") val newPassword: String? = null)