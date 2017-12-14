package br.com.symon.injection.components

import br.com.symon.injection.modules.SendSaleActivityModule
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.send.SendSaleActivity
import br.com.symon.ui.send.SendSalePresenter
import dagger.Component

@ActivityScope
@Component(dependencies = [(ApplicationComponent::class)], modules = [(SendSaleActivityModule::class)])
interface SendSaleActivityComponent {
    fun inject(sendSaleActivity: SendSaleActivity)
    fun sendSalePresenter(): SendSalePresenter
}