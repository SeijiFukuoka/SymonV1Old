package br.com.symon.injection.modules

import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.profile.ProfileContract
import br.com.symon.ui.profile.ProfilePresenter
import dagger.Module
import dagger.Provides

@Module
class ProfileActivityModule(private val view: ProfileContract.View) {
    @Provides
    @ActivityScope
    fun providerView() = view

    @Provides
    @ActivityScope
    fun providerPresenter(view: ProfileContract.View,
                          userRepository: UserRepository) =
            ProfilePresenter(view, userRepository)
}