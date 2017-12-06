package br.com.symon.ui.sales

import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.model.requests.BlockUserRequest
import br.com.symon.data.model.requests.SaleReportRequest
import br.com.symon.data.repository.SaleRepository
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.FragmentScope
import javax.inject.Inject

@FragmentScope
class SalesPresenter @Inject constructor(val view: SalesContract.View, private val saleRepository: SaleRepository, private val userRepository: UserRepository) : SalesContract.Presenter {

    override fun getUser() {
        userRepository.getUserCache().subscribe({
            view.setUser(it)
        }, {
            GeneralErrorHandler(it, view, {})
        })
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
                        in 200..204 -> {
                            view.updateActionSAle(position, true)
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
                        in 200..204 -> {
                            view.updateActionSAle(position, false)
                        }
                    }
                }, {
                    GeneralErrorHandler(it, view, {})
                })
    }

    override fun searchQuerySale(query: String, userToken: String, page: Int, pageSize: Int) {
        saleRepository.searchSale(query, userToken, page, pageSize)
                .subscribe({
                    view.showSearchSales(it)
                }, {
                    GeneralErrorHandler(it, view, {})
                })
    }

    override fun reportSale(userToken: String?, saleReportRequest: SaleReportRequest?) {
        saleRepository.reportSale(saleReportRequest, userToken)
                .subscribe({
                    view.showReportSaleResponse()
                }, {
                    GeneralErrorHandler(it, view, {})
                })
    }

    override fun blockUser(userToken: String?, userBlockedId: BlockUserRequest?) {
        userRepository.blockUSer(userToken, userBlockedId)
                .subscribe({
                    view.showBlockUserResponse()
                }, {
                    GeneralErrorHandler(it, view, {})
                })
    }
}