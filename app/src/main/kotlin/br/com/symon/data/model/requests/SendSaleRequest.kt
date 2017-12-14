package br.com.symon.data.model.requests

import com.google.gson.annotations.SerializedName


data class SendSaleRequest(@SerializedName("message") val productName: String,
                           @SerializedName("place") val placeName: String,
                           @SerializedName("price") val price: Double,
                           @SerializedName("latitude") val lat: Double,
                           @SerializedName("longitude") val lng: Double)