package br.com.symon.injection.components

import br.com.symon.injection.modules.EditProfileActivityModule
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.editProfile.EditProfileActivity
import br.com.symon.ui.editProfile.EditProfilePresenter
import dagger.Component

@ActivityScope
@Component(dependencies = [(ApplicationComponent::class)],
        modules = [(EditProfileActivityModule::class)])
interface EditProfileActivityComponent {
    fun inject(editProfileActivity: EditProfileActivity)
    fun profilePresenter() : EditProfilePresenter
}