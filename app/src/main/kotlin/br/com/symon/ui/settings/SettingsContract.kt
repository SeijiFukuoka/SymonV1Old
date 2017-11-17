package br.com.symon.ui.settings

import br.com.symon.base.BaseView
import br.com.symon.data.model.Settings

interface SettingsContract {
    interface View : BaseView {
        fun showNotificationsSettings(settings: Settings?)
    }

    interface Presenter {
        fun saveNotificationsSettings(settings: Settings)
        fun getNotificationsSettings()
    }
}