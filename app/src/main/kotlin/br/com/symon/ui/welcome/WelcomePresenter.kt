package br.com.symon.ui.welcome

import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import javax.inject.Inject

@ActivityScope
class WelcomePresenter @Inject constructor(view: WelcomeContract.View, userRepository: UserRepository)
    : WelcomeContract.Presenter  {


}