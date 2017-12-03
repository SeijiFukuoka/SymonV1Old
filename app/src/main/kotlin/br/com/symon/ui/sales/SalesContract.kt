package br.com.symon.ui.sales

import br.com.symon.base.BaseView
import br.com.symon.data.model.Sale
import br.com.symon.data.model.requests.BlockUserRequest
import br.com.symon.data.model.requests.SaleReportRequest
import br.com.symon.data.model.responses.SalesListResponse
import br.com.symon.data.model.responses.UserTokenResponse

interface SalesContract {
    interface View : BaseView {
        fun setUser(userTokenResponse: UserTokenResponse)
        fun showSales(salesListResponse: SalesListResponse)
        fun updateActionSAle(position: Int, isLike: Boolean)

        //        TODO("Utilizando até a API de search estar atualizada")
        fun showSearchSales(salesList: MutableList<Sale>)

        fun showReportSaleResponse()
        fun showBlockUserResponse()
    }

    interface Presenter {
        fun getUser()
        fun loadSales(userToken: String, page: Int, pageSize: Int)
        fun likeSale(position: Int, saleId: Int, userToken: String)
        fun disLikeSale(position: Int, saleId: Int, userToken: String)
        fun searchQuerySale(userToken: String, query: String)
        fun reportSale(userToken: String?, saleReportRequest: SaleReportRequest?)
        fun blockUser(userToken: String?, userBlockedId: BlockUserRequest?)
    }
}