package br.com.symon.ui.editProfile

import android.net.Uri
import br.com.symon.base.BaseView
import br.com.symon.data.model.User
import br.com.symon.data.model.requests.UserFullUpdateRequest


interface EditProfileContract {
    interface View : BaseView {
        fun showUserData(user: User?)
        fun showPhoto(photo: String?)
        fun notifyDataUpdate(user: User?)
        fun showErrorMessage(error: String?)
        fun showInvalidPassword()
        fun logout()
    }

    interface Presenter{
        fun getUserCache()
        fun updateUserInfo(userId: Int, userFullUpdateRequest: UserFullUpdateRequest)
        fun uploadUserPhoto(userId: Int, uri: Uri?)
        fun deleteUserCache()
    }
}