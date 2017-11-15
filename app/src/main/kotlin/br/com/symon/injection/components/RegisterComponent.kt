package br.com.symon.injection.components

import br.com.symon.injection.modules.RegisterActivityModule
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.register.RegisterActivity
import br.com.symon.ui.register.RegisterPresenter
import dagger.Component

@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(RegisterActivityModule::class))
interface RegisterComponent {
    fun inject(registerActivity: RegisterActivity)
    fun registerPresenter(): RegisterPresenter
}