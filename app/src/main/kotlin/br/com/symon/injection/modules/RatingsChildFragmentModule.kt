package br.com.symon.injection.modules

import br.com.symon.data.repository.BlockedUsersRepository
import br.com.symon.data.repository.SaleRepository
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.FragmentScope
import br.com.symon.ui.ratings.RatingsChildFragmentContract
import br.com.symon.ui.ratings.RatingsChildFragmentPresenter
import dagger.Module
import dagger.Provides

@Module
class RatingsChildFragmentModule(val view: RatingsChildFragmentContract.View) {

    @Provides
    @FragmentScope
    fun provideView(): RatingsChildFragmentContract.View {
        return view
    }

    @Provides
    @FragmentScope
    fun providePresenter(view: RatingsChildFragmentContract.View, userRepository: UserRepository, saleRepository: SaleRepository, blockedUsersRepository: BlockedUsersRepository): RatingsChildFragmentPresenter {
        return RatingsChildFragmentPresenter(view, userRepository, saleRepository, blockedUsersRepository)
    }
}