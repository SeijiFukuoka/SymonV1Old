package br.com.symon.ui.ratings

import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.FragmentScope
import javax.inject.Inject

@FragmentScope
class RatingsChildFragmentPresenter @Inject constructor(val view: RatingsChildFragmentContract.View, private val userRepository: UserRepository) : RatingsChildFragmentContract.Presenter {
    override fun loadLikes(userToken: String) {
        userRepository.getLikes(userToken)
                .subscribe({
                    view.showLikes(it)
                }, {
                    GeneralErrorHandler(it, view, {})
                })
    }

    override fun getUserCache() {
        userRepository.getUserCache().subscribe({
            view.setUser(it)
        }, {
            GeneralErrorHandler(it, view, {})
        })
    }
}