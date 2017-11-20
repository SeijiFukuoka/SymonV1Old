package br.com.symon.ui.splash

import br.com.symon.data.model.responses.UserTokenResponse


interface SplashContract {
    interface View {
        fun setupNavigation(userTokenResponse: UserTokenResponse)
    }

    interface Presenter {
        fun getUserCache()
    }
}