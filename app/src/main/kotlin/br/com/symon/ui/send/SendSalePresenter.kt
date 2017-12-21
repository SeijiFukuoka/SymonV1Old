package br.com.symon.ui.send

import android.net.Uri
import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.model.requests.SendSaleRequest
import br.com.symon.data.repository.FileRepository
import br.com.symon.data.repository.SaleRepository
import br.com.symon.injection.scope.ActivityScope
import javax.inject.Inject


@ActivityScope
class SendSalePresenter @Inject constructor(private val view: SendSaleContract.View,
                                            private val saleRepository: SaleRepository,
                                            private val fileRepository: FileRepository) :
        SendSaleContract.Presenter {

    override fun sendSale(userToken: String, sendSaleRequest: SendSaleRequest) {
        view.showLoading()
        saleRepository.uploadSale(userToken, sendSaleRequest).subscribe({
            view.hideLoading()
            view.notifySendSuccessfully(it.body()?.id)
        }, {
            view.hideLoading()
            GeneralErrorHandler(it, view, {})
        })
    }

    override fun updateSale(saleId: Int, userToken: String, sendSaleRequest: SendSaleRequest) {
        view.showLoading()
        saleRepository.updateSale(saleId, userToken, sendSaleRequest).subscribe({
            view.hideLoading()
            view.updateSaleSuccessfully()
        })
    }

    override fun uploadUserPhoto(userToken: String, saleId: Int, uri: Uri?) {
        view.showLoading()

        fileRepository.createMultipartFromUri(uri).subscribe {
            saleRepository.uploadSalePhoto(userToken, saleId, it).subscribe({
                view.hideLoading()
                view.notifySendPhotoSuccessfully(it.body()?.uri)
            }, {
                view.hideLoading()
                GeneralErrorHandler(it, view, {})
            })
        }
    }
}