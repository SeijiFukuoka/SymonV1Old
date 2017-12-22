package br.com.symon.data.model

import com.google.gson.annotations.SerializedName


data class Notification(@SerializedName("id") val id: Int,
                        @SerializedName("created_at") val createdAt: String?,
                        @SerializedName("updated_at") val updatedAt: String?,
                        @SerializedName("type_id") val typeId: Int,
                        @SerializedName("sale_id") val saleId: Int,
                        @SerializedName("sale_photo") val salePhoto: String?,
                        @SerializedName("formatted_date") val formattedDate: String?,
                        @SerializedName("user") val user: User) {


    enum class NotificationTypeEnum constructor(val value: Int) {
        LIKE(1),
        DISLIKE(2),
        COMMENT(3),
    }
}