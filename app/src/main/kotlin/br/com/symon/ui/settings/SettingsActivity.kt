package br.com.symon.ui.settings

import android.os.Bundle
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.data.model.Settings
import br.com.symon.injection.components.DaggerSettingsComponent
import br.com.symon.injection.components.SettingsComponent
import br.com.symon.injection.modules.SettingsModule
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.layout_toolbar.*

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

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        settingsComponent.settingsPresenter().getNotificationsSettings()

        imageBackArrow.setOnClickListener {
            onBackPressed()
        }

        settingsItsCheapSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                settingsComponent.settingsPresenter().saveNotificationsSettings(Settings(itsCheapPushNotificationEnable = isChecked))
            }
        }

        settingsItsExpensiveSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                settingsComponent.settingsPresenter().saveNotificationsSettings(Settings(itsExpensivePushNotificationEnable = isChecked))
            }
        }

        settingsCommentsSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                settingsComponent.settingsPresenter().saveNotificationsSettings(Settings(commentsPushNotificationEnable = isChecked))
            }
        }
        settingsBlockedUsersLayout.setOnClickListener {
            TODO("PENDENTE CRIAR ACTIVITY")
        }
    }

    override fun showNotificationsSettings(settings: Settings?) {
        if (settings == null) {
            settingsComponent.settingsPresenter().saveNotificationsSettings(Settings())
        } else {
            settings?.apply {
                settingsItsCheapSwitch.isChecked = itsCheapPushNotificationEnable
                settingsItsExpensiveSwitch.isChecked = itsExpensivePushNotificationEnable
                settingsCommentsSwitch.isChecked = commentsPushNotificationEnable
            }
        }

    }
}