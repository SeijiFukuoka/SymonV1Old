package br.com.symon.ui.blockedUsers

import br.com.symon.CustomApplication
import br.com.symon.base.BaseActivity
import br.com.symon.data.model.User
import br.com.symon.injection.components.BlockedUsersActivityComponent
import br.com.symon.injection.components.DaggerBlockedUsersActivityComponent
import br.com.symon.injection.modules.BlockedUsersActivityModule

class BlockedUsersActivity : BaseActivity(), BlockedUsersContract.View {

    private val blockedUsersActivityComponent: BlockedUsersActivityComponent
        get() = DaggerBlockedUsersActivityComponent
                .builder()
                .applicationComponent((application as CustomApplication).applicationComponent)
                .blockedUsersActivityModule(BlockedUsersActivityModule(this))
                .build()


    override fun showUsersBlocked(usersBlockedList: MutableList<User>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showUnblockUserResponse() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}