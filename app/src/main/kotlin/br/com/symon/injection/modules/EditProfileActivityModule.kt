package br.com.symon.injection.modules

import br.com.symon.data.repository.FileRepository
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import br.com.symon.ui.editProfile.EditProfileContract
import br.com.symon.ui.editProfile.EditProfilePresenter
import dagger.Module
import dagger.Provides

@Module
class EditProfileActivityModule(private val view: EditProfileContract.View) {
    @Provides
    @ActivityScope
    fun providerView() = view

    @Provides
    @ActivityScope
    fun providerPresenter(view: EditProfileContract.View,
                          userRepository: UserRepository,
                          fileRepository: FileRepository) =
            EditProfilePresenter(view, userRepository, fileRepository)
}