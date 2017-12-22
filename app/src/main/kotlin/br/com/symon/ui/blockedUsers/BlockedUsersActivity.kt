package br.com.symon.ui.blockedUsers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.data.model.BlockedUser
import br.com.symon.data.model.requests.BlockUserRequest
import br.com.symon.injection.components.BlockedUsersActivityComponent
import br.com.symon.injection.components.DaggerBlockedUsersActivityComponent
import br.com.symon.injection.modules.BlockedUsersActivityModule
import kotlinx.android.synthetic.main.activity_blocked_users.*
import kotlinx.android.synthetic.main.view_custom_toolbar.*

class BlockedUsersActivity : BaseActivity(), BlockedUsersContract.View, BlockedUsersAdapter.OnItemClickListener {

    companion object {
        private val INTENT_USER_TOKEN = "INTENT_USER_TOKEN"
        lateinit var userToken: String

        fun newIntent(context: Context, userToken: String?): Intent {
            val intent = Intent(context, BlockedUsersActivity::class.java)
            intent.putExtra(INTENT_USER_TOKEN, userToken)
            return intent
        }
    }

    private val blockedUsersActivityComponent: BlockedUsersActivityComponent
        get() = DaggerBlockedUsersActivityComponent
                .builder()
                .applicationComponent((application as CustomApplication).applicationComponent)
                .blockedUsersActivityModule(BlockedUsersActivityModule(this))
                .build()

    private lateinit var blockedUsersAdapter: BlockedUsersAdapter
    private lateinit var unblockUser: BlockedUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blocked_users)

        blockedUsersActivityComponent.inject(this)

        setSupportActionBar(customToolbar).apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }

        if (intent.extras.getString(INTENT_USER_TOKEN) != null) {
            userToken = intent.extras.getString(INTENT_USER_TOKEN)
        }

        setupToolbar()
        getBlockedUsersList()
    }

    override fun showUsersBlocked(usersBlockedList: MutableList<BlockedUser>?) {
        val linearLayoutManager = LinearLayoutManager(this)
        blockedUsersActivityRecyclerView.setHasFixedSize(true)
        blockedUsersActivityRecyclerView.layoutManager = linearLayoutManager
        blockedUsersActivityRecyclerView.isNestedScrollingEnabled = false

        blockedUsersAdapter = BlockedUsersAdapter(usersBlockedList!!, this)
        blockedUsersActivityRecyclerView.adapter = blockedUsersAdapter

        blockedUsersActivityNoContent.visibility = View.GONE
        blockedUsersActivityNestedScrollView.visibility = View.VISIBLE
        hideLoading()
    }

    override fun showNoContentView() {
        blockedUsersActivityNoContent.visibility = View.VISIBLE
        blockedUsersActivityNestedScrollView.visibility = View.GONE
    }

    override fun showUnblockUserResponse() {
        blockedUsersAdapter.remove(unblockUser)

        if (blockedUsersAdapter.itemCount == 0)
            showNoContentView()
    }

    override fun onUnblockUserClick(blockedUser: BlockedUser) {
        unblockUser = blockedUser
        val blockUserRequest = BlockUserRequest(unblockUser.id)
        blockedUsersActivityComponent.providePresenter().unblockUser(userToken, blockUserRequest)
    }

    private fun setupToolbar() {
        customToolbarBackImageView.setOnClickListener {
            onBackPressed()
        }
        customToolbarTitleTextView.text = getString(R.string.blocked_users_activity_toolbar_title)
    }

    private fun getBlockedUsersList() {
        blockedUsersActivityComponent.providePresenter().loadBlockedUsers(userToken)
    }
}