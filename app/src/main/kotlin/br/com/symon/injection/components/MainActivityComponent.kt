package br.com.symon.injection.components

import br.com.symon.injection.modules.MainActivityModule
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.main.MainActivity
import br.com.symon.ui.main.MainPresenter
import dagger.Component

@ActivityScope
@Component(dependencies = [(ApplicationComponent::class)], modules = [(MainActivityModule::class)])
interface MainActivityComponent {
    fun inject(mainActivity: MainActivity)
    fun mainPresenter(): MainPresenter
}