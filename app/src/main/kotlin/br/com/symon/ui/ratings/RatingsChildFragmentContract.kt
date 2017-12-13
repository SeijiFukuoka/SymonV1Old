package br.com.symon.ui.ratings

import br.com.symon.base.BaseView
import br.com.symon.data.model.requests.BlockUserRequest
import br.com.symon.data.model.requests.SaleReportRequest
import br.com.symon.data.model.responses.SalesListResponse
import br.com.symon.data.model.responses.UserTokenResponse

interface RatingsChildFragmentContract {
    interface View : BaseView {
        fun setUser(userTokenResponse: UserTokenResponse)
        fun showTabResponse(salesListResponse: SalesListResponse)
        fun updateActionSAle(position: Int, isLike: Boolean)
        fun showReportSaleResponse()
        fun showBlockUserResponse()
    }

    interface Presenter {
        fun getUserCache()
        fun loadTab(ratingsChildType: RatingsChildFragment.RatingsChildType, userToken: String, page: Int, pageSize: Int, order: Int)
        fun likeSale(position: Int, saleId: Int, userToken: String)
        fun disLikeSale(position: Int, saleId: Int, userToken: String)
        fun reportSale(userToken: String?, saleReportRequest: SaleReportRequest?)
        fun blockUser(userToken: String?, userBlockedId: BlockUserRequest?)
    }
}