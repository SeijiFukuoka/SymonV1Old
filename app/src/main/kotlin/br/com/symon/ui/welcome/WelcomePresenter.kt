package br.com.symon.ui.welcome

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
       /* userRepository.registryUserFacebook(userTokenResponse).subscribe({
            val userId = it.id
            val userCache = User(
                    userId,
                    userTokenResponse.name,
                    userTokenResponse.email,
                    null,
                    null,
                    userTokenResponse.facebookId,
                    null)

            userRepository.saveUserCache(userCache).subscribe({
                view.redirectMainActivity(userCache)
            }, {
                it.printStackTrace()
            })
        }, {
            //Error
            it.printStackTrace()
        })*/
    }


}