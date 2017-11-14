package br.com.symon.ui.login

import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.model.requests.UserTokenRequest
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import javax.inject.Inject

@ActivityScope
class LoginConfirmationPresenter @Inject constructor(val view: LoginConfirmationContract.View, private val userRepository: UserRepository) : LoginConfirmationContract.Presenter {

    override fun getUserToken(userTokenRequest: UserTokenRequest) {
        view.showLoading()
        userRepository.getUserToken(userTokenRequest)
                .subscribe({
                    when (it.code()) {
                        200 -> {
                            it.body()?.user?.let { it1 -> userRepository.saveUserCache(it1) }
                            view.handleTokenResponse(it.body())
                        }
                        401 -> view.handleUserNotFoundResponse()
                    }
                    view.hideLoading()
                }, {
                    GeneralErrorHandler(it, view, {})
                    view.hideLoading()
                })
    }
}