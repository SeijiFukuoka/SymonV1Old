package br.com.symon.ui.sales

import br.com.symon.base.BaseView
import br.com.symon.data.model.responses.SalesListResponse

interface SalesContract {
    interface View : BaseView {
        fun showSales(salesListResponse: SalesListResponse)
    }

    interface Presenter {
        fun loadSales(page: Int, pageSize: Int)
    }
}