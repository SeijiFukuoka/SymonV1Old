package br.com.symon.ui.profile

import android.net.Uri
import br.com.symon.base.BaseView
import br.com.symon.data.model.User
import br.com.symon.data.model.requests.UserFullUpdateRequest


interface ProfileContract {
    interface View : BaseView {
        fun showUserData(user: User?)
        fun showPhoto(photo: String?)
        fun notifyDataUpdate()
        fun showErrorMessage(error: String?)
    }

    interface Presenter{
        fun getUserCache()
        fun updateUserInfo(userId: Int, userFullUpdateRequest: UserFullUpdateRequest)
        fun uploadUserPhoto(userId: Int, uri: Uri?)
    }
}