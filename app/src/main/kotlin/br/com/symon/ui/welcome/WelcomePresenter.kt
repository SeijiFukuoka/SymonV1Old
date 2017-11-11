package br.com.symon.ui.welcome

import br.com.symon.data.model.User
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import javax.inject.Inject

@ActivityScope
class WelcomePresenter @Inject constructor(
        private val view: WelcomeContract.View,
        private val userRepository: UserRepository)
    : WelcomeContract.Presenter {

    override fun registerUser(user: User) {
        userRepository.registryUser(user).subscribe({
            val userId = it.body().id
            val userCache = User(
                    userId,
                    user.name,
                    user.email,
                    user.phone,
                    user.birthday,
                    user.facebookId,
                    user.photo)

            userRepository.saveUserCache(userCache).subscribe({
                view.redirectMainActivity(userCache)
            }, {
                it.printStackTrace()
            })
        }, {
            //Error
            it.printStackTrace()
        })
    }


}