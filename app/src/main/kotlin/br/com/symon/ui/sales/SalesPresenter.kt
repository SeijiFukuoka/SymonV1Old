package br.com.symon.ui.sales

import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.model.requests.BlockUserRequest
import br.com.symon.data.model.requests.SaleReportRequest
import br.com.symon.data.repository.BlockedUsersRepository
import br.com.symon.data.repository.SaleRepository
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.FragmentScope
import javax.inject.Inject

@FragmentScope
class SalesPresenter @Inject constructor(val view: SalesContract.View, private val saleRepository: SaleRepository, private val userRepository: UserRepository, private val blockedUsersRepository: BlockedUsersRepository) : SalesContract.Presenter {

    override fun getUser() {
        userRepository.getUserCache().subscribe({
            view.setUser(it)
        }, {
            GeneralErrorHandler(it, view, {})
        })
    }

    override fun loadSales(userToken: String, page: Int, pageSize: Int, radius: Int, latitude: Double, longitude: Double) {
        saleRepository.getSalesList(userToken, page, pageSize, radius, latitude, longitude)
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


    override fun blockUser(userToken: String, blockedUserRequest: BlockUserRequest) {
        blockedUsersRepository.blockUser(userToken, blockedUserRequest)
                .subscribe({
                    when (it.code()) {
                        in 200..204 -> {
                            view.showBlockUserResponse(blockedUserRequest)
                        }
                        else -> {
                            view.showBlockUserResponseError()
                        }
                    }
                }, {
                    GeneralErrorHandler(it, view, {})
                })
    }
}