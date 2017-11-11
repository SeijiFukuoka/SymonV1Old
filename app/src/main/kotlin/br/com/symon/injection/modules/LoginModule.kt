package br.com.symon.injection.modules

import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.login.LoginContract
import br.com.symon.ui.login.LoginPresenter
import dagger.Module
import dagger.Provides

@Module
class LoginModule(val view: LoginContract.View) {

    @Provides
    @ActivityScope
    fun providerView(): LoginContract.View = view

    @Provides
    @ActivityScope
    fun providePresenter(view: LoginContract.View, userRepository: UserRepository):
            LoginPresenter = LoginPresenter(view, userRepository)
}