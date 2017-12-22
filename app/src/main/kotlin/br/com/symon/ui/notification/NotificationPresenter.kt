package br.com.symon.ui.notification

import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.repository.NotificationRepository
import br.com.symon.injection.scope.FragmentScope
import javax.inject.Inject

@FragmentScope
class NotificationPresenter @Inject constructor(private val view: NotificationContract.View,
                                                private val notificationRepository: NotificationRepository)
    : NotificationContract.Presenter {

    override fun getNotifications(userToken: String) {
        view.showLoading()
        notificationRepository.getNotifications(userToken).subscribe({
            view.hideLoading()
            view.showNotifications(it)
        }, {
            view.hideLoading()
            GeneralErrorHandler(it, view, {})
        })
    }
}