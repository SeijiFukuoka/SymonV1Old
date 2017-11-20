package br.com.symon.injection.modules

import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.retrievePassword.RetrievePasswordContract
import br.com.symon.ui.retrievePassword.RetrievePasswordPresenter
import dagger.Module
import dagger.Provides

@Module
class RetrievePasswordActivityModule(val view: RetrievePasswordContract.View) {
    @Provides
    @ActivityScope
    fun providerView() = view

    @Provides
    @ActivityScope
    fun providerPresenter(view: RetrievePasswordContract.View, userRepository: UserRepository) =
            RetrievePasswordPresenter(view, userRepository)
}