package br.com.symon.ui.saleDetail

import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.model.requests.BlockUserRequest
import br.com.symon.data.model.requests.SendSaleCommentRequest
import br.com.symon.data.repository.SaleRepository
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope

@ActivityScope
class SaleDetailPresenter(val view: SaleDetailContract.View, private val saleRepository: SaleRepository, private val userRepository: UserRepository) : SaleDetailContract.Presenter {

    override fun getComments(saleId: Int) {
        saleRepository.getComments(saleId)
                .subscribe({
                    view.showComments(it)
                }, {
                    GeneralErrorHandler(it, view, {})
                })
    }

    override fun setFavorite() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendComment(saleId: Int, sendSaleCommentRequest: SendSaleCommentRequest) {
        saleRepository.sendComment(saleId, sendSaleCommentRequest)
                .subscribe({
                    view.showSendCommentResponse()
                }, {
                    GeneralErrorHandler(it, view, {})
                })
    }

    override fun blockUser(userToken: String?, userBlockedId: BlockUserRequest?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}