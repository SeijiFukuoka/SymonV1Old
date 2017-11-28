package br.com.symon.ui.main

import br.com.symon.base.BaseView
import br.com.symon.data.model.Sale
import br.com.symon.data.model.responses.UserTokenResponse

interface MainContract {
    interface View : BaseView {
        fun setUser(user: UserTokenResponse?)
        fun showSearchResults(searchResults: MutableList<Sale>)
    }

    interface Presenter {
        fun getUserCache()
        fun searchSales(query: String, userToken: String)
    }
}