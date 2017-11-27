package br.com.symon.ui.sales

import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.repository.SaleRepository
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.FragmentScope
import javax.inject.Inject

@FragmentScope
class SalesPresenter @Inject constructor(val view: SalesContract.View, private val saleRepository: SaleRepository, private val userRepository: UserRepository) : SalesContract.Presenter {

    override fun getUser() {
        userRepository.getUserCache().subscribe {
            view.setUser(it)
        }
    }

    override fun loadSales(userToken: String, page: Int, pageSize: Int) {
        saleRepository.getSalesList(userToken, page, pageSize)
                .subscribe({
                    view.showSales(it)
                }, {
                    GeneralErrorHandler(it, view, {})
                })
    }

    override fun likeSale(position: Int, saleId: Int, userToken: String) {
        saleRepository.likeSale(saleId, userToken)
                .subscribe({
                    when (it.code()) {
                        201 -> {
                            view.updateActionSAle(position, true, true)
                        }
                        204 -> {
                            view.updateActionSAle(position, true, false)
                        }
                    }
                }, {
                    GeneralErrorHandler(it, view, {})
                })
    }

    override fun disLikeSale(position: Int, saleId: Int, userToken: String) {
        saleRepository.disLikeSale(saleId, userToken)
                .subscribe({
                    when (it.code()) {
                        201 -> {
                            view.updateActionSAle(position, false, true)
                        }
                        204 -> {
                            view.updateActionSAle(position, false, false)
                        }
                    }
                }, {
                    GeneralErrorHandler(it, view, {})
                })
    }
}