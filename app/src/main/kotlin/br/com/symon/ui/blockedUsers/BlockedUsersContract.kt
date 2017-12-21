package br.com.symon.ui.blockedUsers

import br.com.symon.base.BaseView

interface BlockedUsersContract {
    interface View : BaseView {
        fun showUsersBlocked()
        fun showUnblockUserResponse()
    }

    interface Presenter {
        fun loadBlockedUsers(userToken: String)
        fun unblockUser(userId: Int)
    }
}