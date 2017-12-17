package br.com.symon.ui.saleDetail

import br.com.symon.base.BaseView
import br.com.symon.data.model.Comment
import br.com.symon.data.model.requests.BlockUserRequest
import br.com.symon.data.model.requests.SendSaleCommentRequest
import br.com.symon.data.model.responses.SendSaleCommentResponse

interface SaleDetailContract {

    interface View : BaseView {
        fun showComments(commentList: MutableList<Comment>)
        fun showFavoriteResponse()
        fun showSendCommentResponse(sendSaleCommentResponse: SendSaleCommentResponse)
        fun showBlockUserResponse()
    }

    interface Presenter {
        fun getComments(userToken: String, saleId: Int)
        fun setFavorite(userToken: String, saleId: Int)
        fun sendComment(userToken: String, saleId: Int, sendSaleCommentRequest: SendSaleCommentRequest)
        fun blockUser(userToken: String?, userBlockedId: BlockUserRequest?)
    }
}