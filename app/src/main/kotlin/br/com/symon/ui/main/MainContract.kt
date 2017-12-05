package br.com.symon.ui.main

import br.com.symon.base.BaseView
import br.com.symon.data.model.responses.SalesListResponse
import br.com.symon.data.model.responses.UserTokenResponse

interface MainContract {
    interface View : BaseView {
        fun setUser(user: UserTokenResponse?)
        fun showSearchResults(salesListResponse: SalesListResponse)
    }

    interface Presenter {
        fun getUserCache()
        fun searchSales(userToken: String, query: String, page: Int, pageSize: Int)
    }
}