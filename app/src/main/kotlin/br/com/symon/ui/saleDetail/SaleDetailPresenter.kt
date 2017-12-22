package br.com.symon.ui.saleDetail

import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.model.requests.BlockUserRequest
import br.com.symon.data.model.requests.SendSaleCommentRequest
import br.com.symon.data.repository.BlockedUsersRepository
import br.com.symon.data.repository.CommentRepository
import br.com.symon.data.repository.SaleRepository
import br.com.symon.injection.scope.ActivityScope

@ActivityScope
class SaleDetailPresenter(val view: SaleDetailContract.View, private val saleRepository: SaleRepository, private val commentRepository: CommentRepository, private val blockedUsersRepository: BlockedUsersRepository) : SaleDetailContract.Presenter {

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

    override fun blockUser(userToken: String, blockedUserRequest: BlockUserRequest) {
        blockedUsersRepository.blockUser(userToken, blockedUserRequest)
                .subscribe({
                    when (it.code()) {
                        in 200..204 -> {
                            view.showBlockUserResponse(blockedUserRequest)
                        }
                        else -> {
                            view.showBlockUserResponseError()
                        }
                    }
                }, {
                    GeneralErrorHandler(it, view, {})
                })
    }

    override fun deleteComment(userToken: String, commentId: Int, position: Int) {
        commentRepository.deleteComment(userToken, commentId)
                .subscribe({
                    when (it.code()) {
                        in 201..204 -> {
                            view.showDeleteCommentResponse(position)
                        }
                    }
                })
    }
}