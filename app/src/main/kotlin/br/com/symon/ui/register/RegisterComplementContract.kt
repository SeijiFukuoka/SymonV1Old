package br.com.symon.ui.register

import android.net.Uri
import br.com.symon.base.BaseView
import br.com.symon.data.model.requests.UserUpdateRequest
import br.com.symon.data.model.responses.UserTokenResponse


interface RegisterComplementContract {
    interface View: BaseView {
        fun goToMain(userTokenResponse: UserTokenResponse?)
        fun showPhoto(photo: String?)
    }

    interface Presenter {
        fun updateUserInfo(userId: Int, userUpdateRequest: UserUpdateRequest)
        fun uploadUserPhoto(userId: Int, uri: Uri?)
    }
}