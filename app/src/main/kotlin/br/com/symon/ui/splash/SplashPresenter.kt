package br.com.symon.ui.splash

import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import javax.inject.Inject

@ActivityScope
class SplashPresenter @Inject constructor(private val view: SplashContract.View,
                                          private val userRepository: UserRepository)
    : SplashContract.Presenter {

    override fun getUserCache() {
        userRepository.getUserCache().subscribe {
            view.setupNavigation(it)
        }
    }
}