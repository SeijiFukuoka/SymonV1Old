package br.com.symon.injection.components

import br.com.symon.injection.modules.ProfileActivityModule
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.profile.ProfileActivity
import br.com.symon.ui.profile.ProfilePresenter
import dagger.Component

@ActivityScope
@Component(dependencies = [(ApplicationComponent::class)],
        modules = [(ProfileActivityModule::class)])
interface ProfileActivityComponent {
    fun inject(profileActivity: ProfileActivity)
    fun profilePresenter() : ProfilePresenter
}