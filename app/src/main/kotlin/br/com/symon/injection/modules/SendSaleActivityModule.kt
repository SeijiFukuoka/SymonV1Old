package br.com.symon.injection.modules

import br.com.symon.data.repository.FileRepository
import br.com.symon.data.repository.SaleRepository
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.send.SendSaleContract
import br.com.symon.ui.send.SendSalePresenter
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@Module
class SendSaleActivityModule @Inject constructor(val view: SendSaleContract.View) {
    @Provides
    @ActivityScope
    fun providerView() = view

    @Provides
    @ActivityScope
    fun providerPresenter(view: SendSaleContract.View,
                          saleRepository: SaleRepository,
                          fileRepository: FileRepository) =
            SendSalePresenter(view, saleRepository, fileRepository)
}