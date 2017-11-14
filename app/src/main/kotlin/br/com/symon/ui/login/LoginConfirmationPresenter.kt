package br.com.symon.ui.login

import br.com.symon.data.model.UserTokenRequest
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import javax.inject.Inject

@ActivityScope
class LoginConfirmationPresenter @Inject constructor(val view: LoginConfirmationContract.View, val userRepositoy: UserRepository) : LoginConfirmationContract.Presenter {

    override fun getUserToken(userTokenRequest: UserTokenRequest) {
        view.showLoading()
        userRepositoy.getUserToken(userTokenRequest)
                .subscribe({
                    when (it.code()) {
                        200 -> {
                            it.body().user?.let { it1 -> userRepositoy.saveUserCache(it1) }
                            view.handleTokenResponse(it.body())
                        }
                        401 -> view.handleUserNotFoundResponse()
                        else -> {
                            view.showError(it.message())
                        }
                    }
                    view.hideLoading()
                }, {
                    view.showError(it.message)
                    view.hideLoading()
                })
    }
}