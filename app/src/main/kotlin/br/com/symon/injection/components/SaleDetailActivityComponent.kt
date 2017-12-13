package br.com.symon.injection.components

import br.com.symon.injection.modules.SaleDetailActivityModule
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.saleDetail.SaleDetailActivity
import br.com.symon.ui.saleDetail.SaleDetailPresenter
import dagger.Component

@ActivityScope
@Component(dependencies = [(ApplicationComponent::class)],
        modules = [(SaleDetailActivityModule::class)])
interface SaleDetailActivityComponent {
    fun inject(saleDetailActivity: SaleDetailActivity)
    fun providePresenter(): SaleDetailPresenter
}