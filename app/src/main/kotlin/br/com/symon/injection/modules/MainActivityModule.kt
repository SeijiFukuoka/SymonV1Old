package br.com.symon.injection.modules

import br.com.symon.data.repository.SaleRepository
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.main.MainContract
import br.com.symon.ui.main.MainPresenter
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule(val view: MainContract.View) {
    @Provides
    @ActivityScope
    fun providerView(): MainContract.View = view

    @Provides
    @ActivityScope
    fun providerPresenter(view: MainContract.View,
                          saleRepository: SaleRepository) = MainPresenter(view, saleRepository)
}