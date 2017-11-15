package br.com.symon.injection.modules

import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.register.RegisterContract
import br.com.symon.ui.register.RegisterPresenter
import dagger.Module
import dagger.Provides

@Module
class RegisterActivityModule(val view: RegisterContract.View) {
    @Provides
    @ActivityScope
    fun providerView() = view

    @Provides
    @ActivityScope
    fun providerPresenter(view: RegisterContract.View, userRepository: UserRepository) =
            RegisterPresenter(view, userRepository)
}