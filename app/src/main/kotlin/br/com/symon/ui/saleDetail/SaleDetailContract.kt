package br.com.symon.ui.saleDetail

import br.com.symon.base.BaseView
import br.com.symon.data.model.Comment
import br.com.symon.data.model.requests.BlockUserRequest
import br.com.symon.data.model.requests.SendSaleCommentRequest

interface SaleDetailContract {

    interface View : BaseView{
        fun showComments(commentList : MutableList<Comment>)
        fun showFavoriteResponse()
        fun showSendCommentResponse()
    }

    interface Presenter{
        fun getComments(saleId : Int)
        fun setFavorite()
        fun sendComment(saleId: Int, sendSaleCommentRequest: SendSaleCommentRequest)
        fun blockUser(userToken: String?, userBlockedId: BlockUserRequest?)
    }

}