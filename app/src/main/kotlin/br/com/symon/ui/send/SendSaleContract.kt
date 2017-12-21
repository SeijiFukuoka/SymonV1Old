package br.com.symon.ui.send

import android.net.Uri
import br.com.symon.base.BaseView
import br.com.symon.data.model.requests.SendSaleRequest

interface SendSaleContract {
    interface View : BaseView {
        fun notifySendSuccessfully(id: Int?)
        fun notifySendPhotoSuccessfully(uri: String?)
        fun updateSaleSuccessfully()
    }

    interface Presenter {
        fun sendSale(userToken: String, sendSaleRequest: SendSaleRequest)
        fun updateSale(saleId: Int, userToken: String, sendSaleRequest: SendSaleRequest)
        fun uploadUserPhoto(userToken: String, saleId: Int, uri: Uri?)
    }
}