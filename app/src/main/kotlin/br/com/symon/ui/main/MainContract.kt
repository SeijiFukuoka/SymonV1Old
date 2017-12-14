package br.com.symon.ui.main

import br.com.symon.base.BaseView
import br.com.symon.data.model.responses.SalesListResponse

interface MainContract {
    interface View : BaseView {
        fun showSearchResults(salesListResponse: SalesListResponse)
    }

    interface Presenter {
        fun searchSales(userToken: String, query: String, page: Int, pageSize: Int)
    }
}