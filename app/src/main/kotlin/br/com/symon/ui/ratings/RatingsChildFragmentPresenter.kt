package br.com.symon.ui.ratings

import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.model.requests.BlockUserRequest
import br.com.symon.data.model.requests.SaleReportRequest
import br.com.symon.data.repository.BlockedUsersRepository
import br.com.symon.data.repository.SaleRepository
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.FragmentScope
import javax.inject.Inject

@FragmentScope
class RatingsChildFragmentPresenter @Inject constructor(val view: RatingsChildFragmentContract.View, private val userRepository: UserRepository, private val saleRepository: SaleRepository, private val blockedUsersRepository: BlockedUsersRepository) : RatingsChildFragmentContract.Presenter {

    override fun getUserCache() {
        userRepository.getUserCache().subscribe({
            view.setUser(it)
        }, {
            GeneralErrorHandler(it, view, {})
        })
    }

    override fun loadTab(ratingsChildType: RatingsChildFragment.RatingsChildType, userToken: String, page: Int, pageSize: Int, order: Int) {
        when (ratingsChildType) {
            RatingsChildFragment.RatingsChildType.FAVORITES -> {
                userRepository.getFavorites(userToken, page, pageSize, order)
                        .subscribe({
                            view.showTabResponse(it)
                        }, {
                            GeneralErrorHandler(it, view, {})
                        })
            }
            RatingsChildFragment.RatingsChildType.LIKES -> {
                userRepository.getLikes(userToken, page, pageSize, order)
                        .subscribe({
                            view.showTabResponse(it)
                        }, {
                            GeneralErrorHandler(it, view, {})
                        })
            }
            RatingsChildFragment.RatingsChildType.DISLIKES -> {
                userRepository.getDislikes(userToken, page, pageSize, order)
                        .subscribe({
                            view.showTabResponse(it)
                        }, {
                            GeneralErrorHandler(it, view, {})
                        })
            }
            RatingsChildFragment.RatingsChildType.COMMENTS -> {
                userRepository.getComments(userToken, page, pageSize, order)
                        .subscribe({
                            view.showTabResponse(it)
                        }, {
                            GeneralErrorHandler(it, view, {})
                        })
            }
        }
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