package br.com.symon.ui.ratings

import br.com.symon.base.BaseView
import br.com.symon.data.model.UserLikeResponse
import br.com.symon.data.model.responses.UserTokenResponse

interface RatingsChildFragmentContract {
    interface View : BaseView {
        fun showLikes(userLikeResponse: MutableList<UserLikeResponse>)
        fun setUser(userTokenResponse: UserTokenResponse)
    }

    interface Presenter {
        fun loadLikes(userToken: String)
        fun getUserCache()
    }
}