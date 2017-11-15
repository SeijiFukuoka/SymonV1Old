package br.com.symon.ui.register

import br.com.symon.base.BaseView
import br.com.symon.data.model.requests.UserUpdateRequest


interface RegisterComplementContract {
    interface View: BaseView {
        fun goToMain()
    }

    interface Presenter {
        fun updateUserInfo(userId: Int, userUpdateRequest: UserUpdateRequest)
    }
}