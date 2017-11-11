package br.com.symon.ui.login

import br.com.symon.data.model.User
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import javax.inject.Inject

@ActivityScope
class LoginPresenter @Inject constructor(val view: LoginContract.View, val userRepositoy: UserRepository) : LoginContract.Presenter {
    override fun login(user: User) {
        view.showLoading()
        userRepositoy.getUser(user.id!!)
                .subscribe({
                    view.showLoginResponse(it)
                    view.hideLoading()
                }, {
                    view.showError(it.message)
                    view.hideLoading()
                })
    }
}