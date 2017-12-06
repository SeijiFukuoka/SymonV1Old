package br.com.symon.injection.components

import br.com.symon.injection.modules.RetrievePasswordActivityModule
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.retrievePassword.RetrievePasswordPresenter
import br.com.symon.ui.retrievePassword.RetrieverPasswordActivity
import dagger.Component

@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(RetrievePasswordActivityModule::class))
interface RetrieverPasswordActivityComponent {
    fun inject(retrieverPasswordActivity: RetrieverPasswordActivity)
    fun retrievePasswordPresenter(): RetrievePasswordPresenter
}