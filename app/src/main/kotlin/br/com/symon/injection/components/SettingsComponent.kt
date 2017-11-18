package br.com.symon.injection.components

import br.com.symon.injection.modules.SettingsModule
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.settings.SettingsActivity
import br.com.symon.ui.settings.SettingsPresenter
import dagger.Component

@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(SettingsModule::class))
interface SettingsComponent {
    fun inject(settingsActivity: SettingsActivity)
    fun settingsPresenter(): SettingsPresenter
}