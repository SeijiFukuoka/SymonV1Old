package br.com.symon.injection.components

import br.com.symon.injection.modules.SalesFragmentModule
import br.com.symon.injection.scope.FragmentScope
import br.com.symon.ui.sales.SalesFragment
import br.com.symon.ui.sales.SalesPresenter
import dagger.Component

@FragmentScope
@Component(dependencies = [(ApplicationComponent::class)], modules = [(SalesFragmentModule::class)])
interface SalesFragmentComponent {
    fun inject(salesFragment: SalesFragment)
    fun salesPresenter(): SalesPresenter
}