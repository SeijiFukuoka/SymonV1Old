package br.com.symon.ui.register

import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.model.requests.UserUpdateRequest
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import javax.inject.Inject

@ActivityScope
class RegisterComplementPresenter @Inject constructor(
        private val view: RegisterComplementContract.View,
        private val userRepository: UserRepository) : RegisterComplementContract.Presenter {

    override fun updateUserInfo(userId: Int, userUpdateRequest: UserUpdateRequest) {
        userRepository.updateUser(userId, userUpdateRequest).subscribe({
            view.goToMain()
        }, {
            GeneralErrorHandler(it, view, {})
        })
    }
}