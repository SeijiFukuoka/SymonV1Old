package br.com.symon.ui.main

import br.com.symon.base.BaseView
import br.com.symon.data.model.User

interface MainContract {
    interface View : BaseView {
        fun setUser(user: User?)
    }

    interface Presenter {
        fun getUserCache()
    }
}