package br.com.symon.ui.retrievePassword

import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import javax.inject.Inject

@ActivityScope
class RetrievePasswordPresenter @Inject constructor(
        private val view: RetrievePasswordContract.View,
        private val userRepository: UserRepository) :
        RetrievePasswordContract.Presenter {

    override fun requestNewPassword(userEmail: String) {
        view.showLoading()
        userRepository.retrievePassword(userEmail).subscribe({
            when (it.code()) {
                in 200..204 -> {
                    view.hideLoading()
                    it.body()?.let { it1 -> view.showRetrievePasswordResponse(it1) }
                }
                else -> {
                    view.showErrorMessage(it.message())
                }
            }
        }, {
            view.hideLoading()
            GeneralErrorHandler(it, view, {})
        })
    }
}