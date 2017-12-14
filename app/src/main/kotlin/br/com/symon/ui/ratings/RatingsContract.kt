package br.com.symon.ui.ratings

import br.com.symon.base.BaseView
import br.com.symon.data.model.responses.UserTokenResponse

interface RatingsContract {
    interface View : BaseView {
        fun setUser(userTokenResponse: UserTokenResponse)
    }

    interface Presenter {
        fun getUser()
    }
}