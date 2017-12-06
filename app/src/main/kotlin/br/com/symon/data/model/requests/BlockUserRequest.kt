package br.com.symon.data.model.requests

import com.google.gson.annotations.SerializedName

data class BlockUserRequest(@SerializedName("blocked_id") var userBlockedId: Int?)