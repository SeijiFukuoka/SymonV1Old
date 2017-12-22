package br.com.symon.ui.notification

import br.com.symon.base.BaseView
import br.com.symon.data.model.Notification


interface NotificationContract {
    interface View: BaseView {
        fun showNotifications(notifications: MutableList<Notification>)
    }

    interface Presenter {
        fun getNotifications(userToken: String)
    }
}