package br.com.symon.ui.welcome

import br.com.symon.base.BaseView
import br.com.symon.data.model.User
import br.com.symon.data.model.requests.UserFacebookRegistryRequest


interface WelcomeContract {
    interface View: BaseView {
        fun redirectMainActivity(user: User)
    }

    interface Presenter {
        fun registerUserFacebook(user: UserFacebookRegistryRequest)
    }
}