package br.com.symon.injection.modules

import br.com.symon.data.repository.NotificationRepository
import br.com.symon.injection.scope.FragmentScope
import br.com.symon.ui.notification.NotificationContract
import br.com.symon.ui.notification.NotificationPresenter
import dagger.Module
import dagger.Provides

@Module
class NotificationFragmentModule(val view: NotificationContract.View) {

    @Provides
    @FragmentScope
    fun providerView() = view

    @Provides
    @FragmentScope
    fun providerPresenter(view: NotificationContract.View, notificationRepository: NotificationRepository) =
            NotificationPresenter(view, notificationRepository)
}