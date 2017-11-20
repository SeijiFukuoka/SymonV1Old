package br.com.symon.ui.retrievePassword

import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.model.User
import br.com.symon.data.model.responses.ErrorResponse
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import com.google.gson.Gson
import javax.inject.Inject

@ActivityScope
class RetrievePasswordPresenter @Inject constructor(
        private val view: RetrievePasswordContract.View,
        private val userRepository: UserRepository) :
        RetrievePasswordContract.Presenter {

    override fun requestNewPassword(userEmail: String) {
        view.showLoading()

        userRepository.retrievePassword(userEmail).subscribe({
            if (it.code() == 200) {
                view.hideLoading()
                view.goToNextStep()
            } else {
                val errorResponse = Gson().fromJson(it.errorBody().toString(), ErrorResponse::class.java)
                view.hideLoading()
                view.showErrorMessage(errorResponse?.error)
            }
        }, {
            view.hideLoading()
            GeneralErrorHandler(it, view, {})
        })
    }
}