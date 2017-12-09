package br.com.symon.ui.main

import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.repository.FileRepository
import br.com.symon.data.repository.SaleRepository
import br.com.symon.injection.scope.ActivityScope
import javax.inject.Inject

@ActivityScope
class MainPresenter @Inject constructor(val view: MainContract.View,
                                        private val saleRepository: SaleRepository,
                                        private val fileRepository: FileRepository) : MainContract.Presenter {

    override fun searchSales(userToken: String, query: String, page: Int, pageSize: Int) {
        saleRepository.searchSale(userToken, query, page, pageSize)
                .subscribe {
                    view.showSearchResults(it)
                }
    }

    override fun setPathUri() {
        fileRepository.setPathUri().subscribe({
            view.callCameraFromUri(it)
        },{
            GeneralErrorHandler(it, view, {})
        })
    }
}