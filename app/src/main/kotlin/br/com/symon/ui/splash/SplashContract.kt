package br.com.symon.ui.splash

import br.com.symon.data.model.User


interface SplashContract {
    interface View {
        fun setupNavigation(user: User)
    }

    interface Presenter {
        fun getUserCache()
    }
}