package br.com.symon.ui.main

import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import javax.inject.Inject

@ActivityScope
class MainPresenter @Inject constructor(val view: MainContract.View, private val userRepository: UserRepository) : MainContract.Presenter {
    override fun getUserCache() {
        userRepository.getUserCache().subscribe {
            view.setUser(it.user)
        }
    }
}