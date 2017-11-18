package br.com.symon.ui.register

import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.model.requests.UserAuthenticateRequest
import br.com.symon.data.model.responses.ErrorResponse
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import com.google.gson.Gson
import javax.inject.Inject

@ActivityScope
class RegisterPresenter @Inject constructor(
        private val view: RegisterContract.View,
        private val userRepository: UserRepository) :
RegisterContract.Presenter {

    override fun register(userAuthenticateRequest: UserAuthenticateRequest) {
        view.showLoading()
        userRepository.registryUser(userAuthenticateRequest).subscribe({
            if (it.code() == 201) {
                view.hideLoading()
                view.goToNextStep(it.body()?.id)
            } else {
                val errorResponse = Gson().fromJson(it.errorBody().toString(), ErrorResponse::class.java)
                view.hideLoading()
                view .showErrorMessage(errorResponse?.error)
            }
        }, {
            view.hideLoading()
            GeneralErrorHandler(it, view, {})
        })
    }
}