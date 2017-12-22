package br.com.symon.injection.modules

import br.com.symon.data.repository.BlockedUsersRepository
import br.com.symon.data.repository.SaleRepository
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.FragmentScope
import br.com.symon.ui.sales.SalesContract
import br.com.symon.ui.sales.SalesPresenter
import dagger.Module
import dagger.Provides

@Module
class SalesFragmentModule(val view: SalesContract.View) {
    @Provides
    @FragmentScope
    fun providerView(): SalesContract.View = view

    @Provides
    @FragmentScope
    fun providerPresenter(view: SalesContract.View,
                          saleRepository: SaleRepository,
                          userRepository: UserRepository,
                          blockedUsersRepository: BlockedUsersRepository) =
            SalesPresenter(view, saleRepository, userRepository, blockedUsersRepository)
}