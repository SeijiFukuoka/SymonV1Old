package br.com.symon.ui.welcome

import br.com.symon.data.model.User
import br.com.symon.data.model.requests.UserFacebookRegistryRequest
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import javax.inject.Inject

@ActivityScope
class WelcomePresenter @Inject constructor(
        private val view: WelcomeContract.View,
        private val userRepository: UserRepository)
    : WelcomeContract.Presenter {

    override fun registerUserFacebook(user: UserFacebookRegistryRequest) {
        userRepository.registryUserFacebook(user).subscribe({
            val userId = it.id
            val userCache = User(
                    userId,
                    user.name,
                    user.email,
                    null,
                    null,
                    user.facebookId,
                    null)

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