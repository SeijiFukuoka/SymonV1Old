package br.com.symon.injection.components

import br.com.symon.injection.modules.ProfileFragmentModule
import br.com.symon.injection.scope.FragmentScope
import br.com.symon.ui.profile.ProfileFragment
import br.com.symon.ui.profile.ProfilePresenter
import dagger.Component

@FragmentScope
@Component(dependencies = [(ApplicationComponent::class)],
        modules = [(ProfileFragmentModule::class)])
interface ProfileFragmentComponent {
    fun inject(profileFragment: ProfileFragment)
    fun profilePresenter(): ProfilePresenter
}
