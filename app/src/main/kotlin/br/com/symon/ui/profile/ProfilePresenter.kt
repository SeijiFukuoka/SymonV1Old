package br.com.symon.ui.profile

import android.net.Uri
import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.model.requests.UserUpdateRequest
import br.com.symon.data.repository.FileRepository
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@ActivityScope
class ProfilePresenter @Inject constructor(private val view: ProfileContract.View,
                                           private val userRepository: UserRepository,
                                           private val fileRepository: FileRepository)
    : ProfileContract.Presenter {

    override fun getUserCache() {
        userRepository.getUserCache().subscribe {
            view.showUserData(it.user)
        }
    }

    override fun updateUserInfo(userId: Int, userUpdateRequest: UserUpdateRequest) {
        view.showLoading()
        userRepository.updateUser(userId, userUpdateRequest).subscribe({
            view.hideLoading()
            val userTokenResponse = it.body()
            userRepository.saveUserCache(userTokenResponse).subscribe({
                view.notifyDataUpdate()
            }, {
                GeneralErrorHandler(it, view, {})
            })
        }, {
            GeneralErrorHandler(it, view, {})
        })
    }

    override fun uploadUserPhoto(userId: Int, uri: Uri?) {
        view.showLoading()
        fileRepository.getPathFromUri(uri).subscribe({
            val file = File(it)

            val filePart = MultipartBody.Part.createFormData(
                    "resource",
                    file.name,
                    RequestBody.create(MediaType.parse("image/jpeg"), file))

            userRepository.uploadUserPhoto(userId, filePart).subscribe({
                view.hideLoading()
                view.showPhoto(it.uri)
            }, {
                GeneralErrorHandler(it, view, {})
            })
        },{
            GeneralErrorHandler(it, view, {})
        })
    }
}