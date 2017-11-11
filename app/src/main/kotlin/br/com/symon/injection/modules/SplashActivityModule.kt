package br.com.symon.injection.modules

import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.splash.SplashContract
import br.com.symon.ui.splash.SplashPresenter
import dagger.Module
import dagger.Provides

@Module
class SplashActivityModule(private val view: SplashContract.View) {
    @Provides
    @ActivityScope
    fun providerView() = view

    @Provides
    @ActivityScope
    fun providerPresenter(view: SplashContract.View, userRepository: UserRepository) =
            SplashPresenter(view, userRepository)
}