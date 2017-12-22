package br.com.symon.injection.modules

import br.com.symon.data.repository.BlockedUsersRepository
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.blockedUsers.BlockedUsersContract
import br.com.symon.ui.blockedUsers.BlockedUsersPresenter
import dagger.Module
import dagger.Provides

@Module
class BlockedUsersActivityModule(val view: BlockedUsersContract.View) {

    @Provides
    @ActivityScope
    fun provideView(): BlockedUsersContract.View = view

    @Provides
    @ActivityScope
    fun providePresenter(view: BlockedUsersContract.View,
                         blockedUsersRepository: BlockedUsersRepository) =
            BlockedUsersPresenter(view, blockedUsersRepository)
}