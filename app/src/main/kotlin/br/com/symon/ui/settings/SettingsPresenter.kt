package br.com.symon.ui.settings

import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.model.Settings
import br.com.symon.data.repository.SettingsRepository
import javax.inject.Inject

class SettingsPresenter @Inject constructor(val view: SettingsContract.View, val repository: SettingsRepository) : SettingsContract.Presenter {
    override fun saveNotificationsSettings(settings: Settings) {
        repository.saveNotificationsSettings(settings).subscribe()
    }

    override fun getNotificationsSettings() {
        repository.getNotificationsSettings().subscribe({
            view.showNotificationsSettings(it)
        }, {
            GeneralErrorHandler(it, view, {})
        })
    }
}