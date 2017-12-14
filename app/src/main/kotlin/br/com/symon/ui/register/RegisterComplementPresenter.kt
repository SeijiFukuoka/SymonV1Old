package br.com.symon.ui.register

import android.net.Uri
import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.model.requests.UserUpdateRequest
import br.com.symon.data.repository.FileRepository
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import javax.inject.Inject

@ActivityScope
class RegisterComplementPresenter @Inject constructor(
        private val view: RegisterComplementContract.View,
        private val userRepository: UserRepository,
        private val fileRepository: FileRepository) : RegisterComplementContract.Presenter {

    override fun updateUserInfo(userId: Int, userUpdateRequest: UserUpdateRequest) {
        view.showLoading()
        userRepository.updateUser(userId, userUpdateRequest).subscribe({
            view.hideLoading()
            val userTokenResponse = it.body()
            userRepository.saveUserCache(userTokenResponse).subscribe({
                view.goToMain(userTokenResponse)
            }, {
                GeneralErrorHandler(it, view, {})
            })
        }, {
            GeneralErrorHandler(it, view, {})
        })
    }

    override fun uploadUserPhoto(userId: Int, uri: Uri?) {
        view.showLoading()
        fileRepository.createMultipartFromUri(uri).subscribe {
            userRepository.uploadUserPhoto(userId, it).subscribe({
                view.hideLoading()
                view.showPhoto(it.uri)
            }, {
                GeneralErrorHandler(it, view, {})
            })
        }
    }
}