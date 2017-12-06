package br.com.symon.ui.settings

import android.os.Bundle
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.common.toast
import br.com.symon.data.model.Settings
import br.com.symon.injection.components.DaggerSettingsComponent
import br.com.symon.injection.components.SettingsComponent
import br.com.symon.injection.modules.SettingsModule
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.view_custom_toolbar.*

class SettingsActivity : BaseActivity(), SettingsContract.View {

    private val settingsComponent: SettingsComponent
        get() = DaggerSettingsComponent
                .builder()
                .applicationComponent((this.application as CustomApplication).applicationComponent)
                .settingsModule(SettingsModule(this))
                .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        settingsComponent.inject(this)

        setSupportActionBar(customToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        settingsComponent.settingsPresenter().getNotificationsSettings()

        customToolbarTitleTextView.text = getString(R.string.settings_toolbar_title)

        customToolbarBackImageView.setOnClickListener {
            onBackPressed()
        }

        settingsItsCheapSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                settingsComponent.settingsPresenter().saveNotificationsSettings(
                        Settings(itsExpensivePushNotificationEnable = settingsItsExpensiveSwitch.isChecked,
                                itsCheapPushNotificationEnable = isChecked,
                                commentsPushNotificationEnable = settingsCommentsSwitch.isChecked))
            }
        }

        settingsItsExpensiveSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                settingsComponent.settingsPresenter().saveNotificationsSettings(
                        Settings(itsExpensivePushNotificationEnable = isChecked,
                                itsCheapPushNotificationEnable = settingsItsCheapSwitch.isChecked,
                                commentsPushNotificationEnable = settingsCommentsSwitch.isChecked))
            }
        }

        settingsCommentsSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                settingsComponent.settingsPresenter().saveNotificationsSettings(
                        Settings(itsExpensivePushNotificationEnable = settingsItsExpensiveSwitch.isChecked,
                                itsCheapPushNotificationEnable = settingsItsCheapSwitch.isChecked,
                                commentsPushNotificationEnable = isChecked))
            }
        }
        settingsBlockedUsersLayout.setOnClickListener {
            toast("Pendente")
//            TODO("PENDENTE CRIAR ACTIVITY")
        }
    }

    override fun showNotificationsSettings(settings: Settings?) {
        settings?.apply {
            settingsItsCheapSwitch.isChecked = itsCheapPushNotificationEnable
            settingsItsExpensiveSwitch.isChecked = itsExpensivePushNotificationEnable
            settingsCommentsSwitch.isChecked = commentsPushNotificationEnable
        }
    }
}