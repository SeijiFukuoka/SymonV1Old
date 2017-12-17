package br.com.symon.ui.saleDetail

import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.model.requests.BlockUserRequest
import br.com.symon.data.model.requests.SendSaleCommentRequest
import br.com.symon.data.repository.SaleRepository
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope

@ActivityScope
class SaleDetailPresenter(val view: SaleDetailContract.View, private val saleRepository: SaleRepository, private val userRepository: UserRepository) : SaleDetailContract.Presenter {

    override fun getComments(userToken: String, saleId: Int) {
        saleRepository.getComments(userToken, saleId)
                .subscribe({
                    view.showComments(it)
                }, {
                    GeneralErrorHandler(it, view, {})
                })
    }

    override fun setFavorite(userToken: String, saleId: Int) {
        saleRepository.setFavorite(userToken, saleId)
                .subscribe({
                    when (it.code()) {
                        in 201..204 -> {
                            view.showFavoriteResponse()
                        }
                    }
                })
    }

    override fun sendComment(userToken: String, saleId: Int, sendSaleCommentRequest: SendSaleCommentRequest) {
        saleRepository.sendComment(userToken, saleId, sendSaleCommentRequest)
                .subscribe({
                    view.showSendCommentResponse(it)
                }, {
                    GeneralErrorHandler(it, view, {})
                })
    }

    override fun blockUser(userToken: String?, userBlockedId: BlockUserRequest?) {
        userRepository.blockUSer(userToken, userBlockedId)
                .subscribe({
                    view.showBlockUserResponse()
                }, {
                    GeneralErrorHandler(it, view, {})
                })
    }
}