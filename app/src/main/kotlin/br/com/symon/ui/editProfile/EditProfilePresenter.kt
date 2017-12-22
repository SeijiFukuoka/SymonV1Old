package br.com.symon.ui.editProfile

import android.net.Uri
import br.com.gold360.financas.common.GeneralErrorHandler
import br.com.symon.data.model.requests.UserFullUpdateRequest
import br.com.symon.data.model.responses.ErrorResponse
import br.com.symon.data.repository.FileRepository
import br.com.symon.data.repository.UserRepository
import br.com.symon.injection.scope.ActivityScope
import com.google.gson.Gson
import javax.inject.Inject

@ActivityScope
class EditProfilePresenter @Inject constructor(private val view: EditProfileContract.View,
                                               private val userRepository: UserRepository,
                                               private val fileRepository: FileRepository)
    : EditProfileContract.Presenter {

    override fun deleteUserCache() {
        userRepository.deleteCache().subscribe{
            view.logout()
        }
    }

    override fun getUserCache() {
        userRepository.getUserCache().subscribe {
            view.showUserData(it.user)
        }
    }

    override fun updateUserInfo(userId: Int, userFullUpdateRequest: UserFullUpdateRequest) {
        view.showLoading()
        userRepository.updateFullUser(userId, userFullUpdateRequest).subscribe({
            view.hideLoading()

            when(it.code()) {
                200 -> {
                    val userTokenResponse = it.body()
                    userRepository.saveUserCache(userTokenResponse).subscribe({
                        view.notifyDataUpdate(userTokenResponse?.user)
                    }, {
                        GeneralErrorHandler(it, view, {})
                    })
                }
                401 -> {
                    view.showInvalidPassword()
                }
                else -> {
                    val errorResponse = Gson().fromJson(it.errorBody()?.string(), ErrorResponse::class.java)
                    view.hideLoading()
                    view.showErrorMessage(errorResponse?.error)
                }
            }
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