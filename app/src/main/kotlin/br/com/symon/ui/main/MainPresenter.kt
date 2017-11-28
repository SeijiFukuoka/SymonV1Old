package br.com.symon.ui.main

import br.com.symon.data.repository.SaleRepository
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import javax.inject.Inject

@ActivityScope
class MainPresenter @Inject constructor(val view: MainContract.View, private val userRepository: UserRepository, private val saleRepository: SaleRepository) : MainContract.Presenter {
    override fun getUserCache() {
        userRepository.getUserCache().subscribe {
            view.setUser(it)
        }
    }

    override fun searchSales(query: String, userToken: String) {
        saleRepository.searchSale(query, userToken)
                .subscribe {
                    view.showSearchResults(it)
                }
    }
}