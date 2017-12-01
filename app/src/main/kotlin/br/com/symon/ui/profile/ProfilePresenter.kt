package br.com.symon.ui.profile

import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import javax.inject.Inject

@ActivityScope
class ProfilePresenter @Inject constructor(private val view: ProfileContract.View,
                                           private val userRepository: UserRepository)
    : ProfileContract.Presenter {

    override fun getUserCache() {
        userRepository.getUserCache().subscribe {
            view.showUserData(it.user)
        }
    }
}