package br.com.symon.injection.modules

import br.com.symon.data.repository.SaleRepository
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.saleDetail.SaleDetailContract
import br.com.symon.ui.saleDetail.SaleDetailPresenter
import dagger.Module
import dagger.Provides

@Module
class SaleDetailActivityModule(val view: SaleDetailContract.View) {

    @Provides
    @ActivityScope
    fun provideView(): SaleDetailContract.View = view

    @Provides
    @ActivityScope
    fun providePresenter(view: SaleDetailContract.View,
                         saleRepository: SaleRepository,
                         userRepository: UserRepository) =
            SaleDetailPresenter(view, saleRepository, userRepository)
}