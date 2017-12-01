package br.com.symon.ui.profile

import br.com.symon.base.BaseView
import br.com.symon.data.model.User


interface ProfileContract {
    interface View : BaseView {
        fun showUserData(user: User?)
    }

    interface Presenter{
        fun getUserCache()
    }
}