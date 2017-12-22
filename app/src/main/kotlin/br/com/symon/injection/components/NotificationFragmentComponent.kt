package br.com.symon.injection.components

import br.com.symon.injection.modules.NotificationFragmentModule
import br.com.symon.injection.scope.FragmentScope
import br.com.symon.ui.notification.NotificationFragment
import br.com.symon.ui.notification.NotificationPresenter
import dagger.Component

@FragmentScope
@Component(dependencies = [(ApplicationComponent::class)],
        modules = [(NotificationFragmentModule::class)])
interface NotificationFragmentComponent {
    fun inject(notificationFragment: NotificationFragment)
    fun notificationPresenter() : NotificationPresenter
}