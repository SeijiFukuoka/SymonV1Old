package br.com.symon.data.model.responses

import com.google.gson.annotations.SerializedName


data class UploadPhotoResponse(@SerializedName("uri") val uri: String?)
