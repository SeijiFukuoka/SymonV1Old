package br.com.symon.injection.components

import br.com.symon.injection.modules.RatingsChildFragmentModule
import br.com.symon.injection.scope.FragmentScope
import br.com.symon.ui.ratings.RatingsChildFragment
import br.com.symon.ui.ratings.RatingsChildFragmentPresenter
import dagger.Component

@FragmentScope
@Component(dependencies = [(ApplicationComponent::class)], modules = [(RatingsChildFragmentModule::class)])
interface RatingsChildFragmentComponent {
    fun inject(ratingsChildFragment: RatingsChildFragment)
    fun ratingsChildFragmentPresenter(): RatingsChildFragmentPresenter
}