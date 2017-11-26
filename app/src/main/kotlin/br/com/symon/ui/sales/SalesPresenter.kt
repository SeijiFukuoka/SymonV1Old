package br.com.symon.ui.sales

import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.repository.SaleRepository
import br.com.symon.injection.scope.FragmentScope
import javax.inject.Inject

@FragmentScope
class SalesPresenter @Inject constructor(val view: SalesContract.View, private val saleRepository: SaleRepository) : SalesContract.Presenter {

    override fun loadSales(page: Int, pageSize: Int) {
        saleRepository.getSalesList(page, pageSize)
                .subscribe({
                    view.showSales(it)
                }, {
                    GeneralErrorHandler(it, view, {})
                })
    }
}