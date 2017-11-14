package br.com.symon.injection.components

import br.com.symon.injection.modules.LoginConfirmationModule
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.login.LoginConfirmationActivity
import br.com.symon.ui.login.LoginConfirmationPresenter
import dagger.Component

@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(LoginConfirmationModule::class))
interface LoginConfirmationComponent {
    fun inject(loginConfirmationActivity: LoginConfirmationActivity)
    fun loginConfirmationPresenter(): LoginConfirmationPresenter
}