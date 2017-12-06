package br.com.symon.ui.profile

import android.net.Uri
import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.model.requests.UserFullUpdateRequest
import br.com.symon.data.model.responses.ErrorResponse
import br.com.symon.data.repository.FileRepository
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import com.google.gson.Gson
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

    override fun updateUserInfo(userId: Int, userFullUpdateRequest: UserFullUpdateRequest) {
        view.showLoading()
        userRepository.updateFullUser(userId, userFullUpdateRequest).subscribe({
            view.hideLoading()

            if (it.code() == 200) {
                val userTokenResponse = it.body()
                userRepository.saveUserCache(userTokenResponse).subscribe({
                    view.notifyDataUpdate()
                }, {
                    GeneralErrorHandler(it, view, {})
                })
            } else {
                val errorResponse = Gson().fromJson(it.errorBody()?.string(), ErrorResponse::class.java)
                view.hideLoading()
                view.showErrorMessage(errorResponse?.error)
            }

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