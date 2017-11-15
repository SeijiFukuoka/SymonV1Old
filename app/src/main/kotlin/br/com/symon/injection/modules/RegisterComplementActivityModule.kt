package br.com.symon.injection.modules

import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.register.RegisterComplementContract
import br.com.symon.ui.register.RegisterComplementPresenter
import dagger.Module
import dagger.Provides

@Module
class RegisterComplementActivityModule(val view: RegisterComplementContract.View) {
    @Provides
    @ActivityScope
    fun providerView() = view

    @Provides
    @ActivityScope
    fun providerPresenter(view: RegisterComplementContract.View, userRepository: UserRepository) =
            RegisterComplementPresenter(view, userRepository)
}