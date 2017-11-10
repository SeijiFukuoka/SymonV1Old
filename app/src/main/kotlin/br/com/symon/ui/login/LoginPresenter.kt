package br.com.symon.ui.login

import br.com.symon.data.model.User
import br.com.symon.injection.scope.PerActivity
import javax.inject.Inject

@PerActivity
class LoginPresenter @Inject constructor(val view: LoginContract.View) : LoginContract.Presenter {
  override fun login(user: User?) {
    view.showLoading()
    TODO("Adicionar Repository")
    view.hideLoading()
  }
}