package br.com.symon.injection.components

import br.com.symon.injection.modules.LoginModule
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.login.LoginActivity
import br.com.symon.ui.login.LoginPresenter
import dagger.Component

@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(LoginModule::class))
interface LoginComponent {
    fun inject(loginActivity: LoginActivity)
    fun loginPresenter(): LoginPresenter
}