package br.com.symon.ui.sales

import br.com.symon.base.BaseView
import br.com.symon.data.model.requests.BlockUserRequest
import br.com.symon.data.model.requests.SaleReportRequest
import br.com.symon.data.model.responses.SalesListResponse
import br.com.symon.data.model.responses.UserTokenResponse

interface SalesContract {
    interface View : BaseView {
        fun setUser(userTokenResponse: UserTokenResponse)
        fun showSales(salesListResponse: SalesListResponse)
        fun updateActionSAle(position: Int, isLike: Boolean)
        fun showSearchSales(salesListResponse: SalesListResponse)

        fun showReportSaleResponse()
        fun showBlockUserResponse()
    }

    interface Presenter {
        fun getUser()
        fun loadSales(userToken: String, page: Int, pageSize: Int)
        fun likeSale(position: Int, saleId: Int, userToken: String)
        fun disLikeSale(position: Int, saleId: Int, userToken: String)
        fun searchQuerySale(query: String, userToken: String, page: Int, pageSize: Int)
        fun reportSale(userToken: String?, saleReportRequest: SaleReportRequest?)
        fun blockUser(userToken: String?, userBlockedId: BlockUserRequest?)
    }
}