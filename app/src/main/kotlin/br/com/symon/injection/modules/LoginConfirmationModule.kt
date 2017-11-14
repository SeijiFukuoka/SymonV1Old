package br.com.symon.injection.modules

import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.login.LoginConfirmationContract
import br.com.symon.ui.login.LoginConfirmationPresenter
import dagger.Module
import dagger.Provides

@Module
class LoginConfirmationModule(val view: LoginConfirmationContract.View) {

    @Provides
    @ActivityScope
    fun providerView(): LoginConfirmationContract.View = view

    @Provides
    @ActivityScope
    fun providePresenter(view: LoginConfirmationContract.View, userRepository: UserRepository):
            LoginConfirmationPresenter = LoginConfirmationPresenter(view, userRepository)
}