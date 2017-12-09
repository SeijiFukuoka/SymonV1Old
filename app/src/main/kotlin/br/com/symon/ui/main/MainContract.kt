package br.com.symon.ui.main

import android.net.Uri
import br.com.symon.base.BaseView
import br.com.symon.data.model.responses.SalesListResponse

interface MainContract {
    interface View : BaseView {
        fun showSearchResults(salesListResponse: SalesListResponse)
        fun callCameraFromUri(uri: Uri?)
    }

    interface Presenter {
        fun searchSales(userToken: String, query: String, page: Int, pageSize: Int)
        fun setPathUri()
    }
}