package br.com.symon.injection.components

import br.com.symon.injection.modules.LoginActivityModule
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.login.LoginActivity
import br.com.symon.ui.login.LoginPresenter
import dagger.Component

@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(LoginActivityModule::class))
interface LoginActivityComponent {
    fun inject(loginActivity: LoginActivity)
    fun loginPresenter(): LoginPresenter
}