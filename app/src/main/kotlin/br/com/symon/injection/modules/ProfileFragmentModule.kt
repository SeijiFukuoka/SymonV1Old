package br.com.symon.injection.modules

import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.FragmentScope
import br.com.symon.ui.profile.ProfileContract
import br.com.symon.ui.profile.ProfilePresenter
import dagger.Module
import dagger.Provides

@Module
class ProfileFragmentModule(val view: ProfileContract.View) {

    @Provides
    @FragmentScope
    fun providerView() = view

    @Provides
    @FragmentScope
    fun providerPresenter(view: ProfileContract.View,
                          userRepository: UserRepository) =
            ProfilePresenter(view, userRepository)
}