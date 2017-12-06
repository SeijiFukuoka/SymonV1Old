package br.com.symon.data.model.responses

import com.google.gson.annotations.SerializedName


data class ErrorResponse(@SerializedName("error") var error: String)