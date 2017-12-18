package br.com.symon.ui.profile

import br.com.symon.base.BaseView
import br.com.symon.data.model.responses.SalesListResponse
import br.com.symon.data.model.responses.UserTokenResponse


interface ProfileContract {
    interface View: BaseView {
        fun showSaleList(saleListResponse: SalesListResponse)
        fun showUserData(user: UserTokenResponse?)
    }

    interface Presenter{
        fun loadSaleList(userToken: String, page: Int, pageSize: Int)
        fun getUserCache()
    }
}