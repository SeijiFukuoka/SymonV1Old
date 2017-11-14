package br.com.symon.injection.modules

import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.welcome.WelcomeContract
import br.com.symon.ui.welcome.WelcomePresenter
import dagger.Module
import dagger.Provides

@Module
class WelcomeActivityModule(private val view: WelcomeContract.View) {
    @Provides
    @ActivityScope
    fun providerView() : WelcomeContract.View = view

    @Provides
    @ActivityScope
    fun providerPresenter(view: WelcomeContract.View, userRepository: UserRepository) = WelcomePresenter(view, userRepository)
}