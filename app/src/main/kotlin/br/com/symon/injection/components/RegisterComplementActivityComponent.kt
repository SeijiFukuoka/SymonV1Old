package br.com.symon.injection.components

import br.com.symon.injection.modules.RegisterComplementActivityModule
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.register.RegisterComplementActivity
import br.com.symon.ui.register.RegisterComplementPresenter
import dagger.Component

@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class),
        modules = arrayOf(RegisterComplementActivityModule::class))
interface RegisterComplementActivityComponent {
    fun inject(registerComplementActivity: RegisterComplementActivity)
    fun registerComplementPresenter(): RegisterComplementPresenter
}