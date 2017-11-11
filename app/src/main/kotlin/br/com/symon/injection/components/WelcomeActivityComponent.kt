package br.com.symon.injection.components

import br.com.symon.injection.modules.WelcomeActivityModule
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.welcome.WelcomeActivity
import br.com.symon.ui.welcome.WelcomePresenter
import dagger.Component


@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(WelcomeActivityModule::class))
interface WelcomeActivityComponent {
    fun inject(welcomeActivity: WelcomeActivity)
    fun welcomePresenter(): WelcomePresenter
}