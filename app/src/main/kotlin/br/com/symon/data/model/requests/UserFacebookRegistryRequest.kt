package br.com.symon.data.model.requests

import com.google.gson.annotations.SerializedName


data class UserFacebookRegistryRequest(@SerializedName("name") val name: String,
                                       @SerializedName("email") val email: String,
                                       @SerializedName("facebookId") val facebookId: String?)