package br.com.symon.injection.components

import br.com.symon.injection.modules.SplashActivityModule
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.splash.SplashActivity
import br.com.symon.ui.splash.SplashPresenter
import dagger.Component

@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(SplashActivityModule::class))
interface SplashComponent {
    fun inject(splashActivity: SplashActivity)
    fun splashPresenter(): SplashPresenter
}