package br.com.symon.injection.modules

import br.com.symon.data.repository.SettingsRepository
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.settings.SettingsContract
import br.com.symon.ui.settings.SettingsPresenter
import dagger.Module
import dagger.Provides

@Module
class SettingsModule(val view: SettingsContract.View) {
    @Provides
    @ActivityScope
    fun providerView(): SettingsContract.View = view

    @Provides
    @ActivityScope
    fun providerPresenter(view: SettingsContract.View, settingsRepository: SettingsRepository) = SettingsPresenter(view, settingsRepository)
}