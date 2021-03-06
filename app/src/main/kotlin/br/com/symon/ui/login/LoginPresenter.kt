package br.com.symon.ui.login

import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import javax.inject.Inject

@ActivityScope
class LoginPresenter @Inject constructor(val view: LoginContract.View, val userRepositoy: UserRepository) : LoginContract.Presenter {

    override fun checkUser(email: String) {
        view.showLoading()
        userRepositoy.checkUser(email)
                .subscribe({
                    view.handleCheckResponse(it)
                    view.hideLoading()
                }, {
                    GeneralErrorHandler(it, view, {})
                    view.hideLoading()
                })
    }
}