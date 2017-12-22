package br.com.symon.ui.blockedUsers

import br.com.symon.base.BaseView
import br.com.symon.data.model.BlockedUser
import br.com.symon.data.model.requests.BlockUserRequest

interface BlockedUsersContract {
    interface View : BaseView {
        fun showUsersBlocked(usersBlockedList: MutableList<BlockedUser>?)
        fun showNoContentView()
        fun showUnblockUserResponse()
    }

    interface Presenter {
        fun loadBlockedUsers(userToken: String)
        fun unblockUser(userToken: String, blockedUserRequest: BlockUserRequest)
    }
}