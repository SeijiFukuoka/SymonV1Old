package br.com.symon.injection.components

import br.com.symon.injection.modules.BlockedUsersActivityModule
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.blockedUsers.BlockedUsersActivity
import br.com.symon.ui.blockedUsers.BlockedUsersPresenter
import dagger.Component

@ActivityScope
@Component(dependencies = [(ApplicationComponent::class)],
        modules = [(BlockedUsersActivityModule::class)])
interface BlockedUsersActivityComponent {
    fun inject(blockedUsersActivity: BlockedUsersActivity)
    fun providePresenter(): BlockedUsersPresenter
}