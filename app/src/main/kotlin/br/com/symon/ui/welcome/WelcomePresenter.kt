package br.com.symon.ui.welcome

import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.model.requests.UserFacebookAuthenticateRequest
import br.com.symon.data.model.responses.ErrorResponse
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import com.google.gson.Gson
import javax.inject.Inject

@ActivityScope
class WelcomePresenter @Inject constructor(
        private val view: WelcomeContract.View,
        private val userRepository: UserRepository)
    : WelcomeContract.Presenter {

    override fun getUserToken(userAuthenticateRequest: UserFacebookAuthenticateRequest) {
        view.showLoading()
        userRepository.getUserToken(userAuthenticateRequest)
                .subscribe({
                    when (it.code()) {
                        200 -> {
                            val userTokenResponse = it.body()
                            userRepository.saveUserCache(userTokenResponse).subscribe ({
                                view.handleTokenResponse(userTokenResponse)
                            }, {
                                GeneralErrorHandler(it, view, {})
                            })
                        }
                        401 -> view.handleUserNotFoundResponse()
                    }
                    view.hideLoading()
                }, {
                    GeneralErrorHandler(it, view, {})
                    view.hideLoading()
                })
    }


    override fun register(userAuthenticateRequest: UserFacebookAuthenticateRequest) {
        view.showLoading()
        userRepository.registryUserFacebook(userAuthenticateRequest).subscribe({
            if (it.code() == 201) {
                view.hideLoading()
                view.goToNextStep(it.body())
            } else {
                val errorResponse : ErrorResponse = Gson().fromJson(it.errorBody()?.string(), ErrorResponse::class.java)
                view.hideLoading()
                view.showErrorMessage(errorResponse.error)
            }
        }, {
            view.hideLoading()
            GeneralErrorHandler(it, view, {})
        })
    }
}