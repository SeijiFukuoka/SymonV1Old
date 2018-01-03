package br.com.symon.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseFragment
import br.com.symon.common.inflate
import br.com.symon.common.loadUrlToBeRounded
import br.com.symon.common.widget.EndlessScrollListener
import br.com.symon.data.model.Constants
import br.com.symon.data.model.responses.SalesListResponse
import br.com.symon.data.model.responses.UserTokenResponse
import br.com.symon.injection.components.DaggerProfileFragmentComponent
import br.com.symon.injection.components.ProfileFragmentComponent
import br.com.symon.injection.modules.ProfileFragmentModule
import br.com.symon.ui.editProfile.EditProfileActivity
import br.com.symon.ui.profile.adapter.ProfileSalesAdapter
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment(), ProfileContract.View {

    companion object {
        const val USER_EXTRA = "user_extra"
        const val USER_EDIT_REQUEST = 14325

        private lateinit var profileSalesAdapter: ProfileSalesAdapter
        private lateinit var userTokenResponse: UserTokenResponse
    }

    private val profileFragmentComponent: ProfileFragmentComponent
        get() = DaggerProfileFragmentComponent
                .builder()
                .applicationComponent((activity.application as CustomApplication).applicationComponent)
                .profileFragmentModule(ProfileFragmentModule(this))
                .build()

    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentId = ProfileFragment::class.java.canonicalName

        profileFragmentComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            container?.inflate(R.layout.fragment_profile)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileEditTextView.setOnClickListener {
            startActivityForResult(Intent(activity, EditProfileActivity::class.java), USER_EDIT_REQUEST)
        }
        profileFragmentComponent.profilePresenter().getUserCache()
    }

    override fun showUserData(user: UserTokenResponse?) {
        user?.let {
            userTokenResponse = it
        }

        userTokenResponse.user?.apply {
            profileNameTextView.text = name
            profileImageView.loadUrlToBeRounded(photoUri, R.drawable.ic_profile_placeholder)
        }

        fetchData(currentPage)
    }

    override fun showSaleList(saleListResponse: SalesListResponse) {
        if (saleListResponse.salesList.size > 0) {
            setupRecyclerView()
            profileMessageTextView.text = getString(R.string.profile_my_posts_label)
            profileSalesAdapter.setList(saleListResponse.salesList)
        } else {
            profileMessageTextView.text = getString(R.string.profile_message)
        }
        profileMessageTextView.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context)
        profileSalesAdapter = ProfileSalesAdapter()

        profileMyPostsRecyclerView.setHasFixedSize(true)
        profileMyPostsRecyclerView.layoutManager = linearLayoutManager
        profileMyPostsRecyclerView.isNestedScrollingEnabled = false
        profileMyPostsRecyclerView.adapter = profileSalesAdapter
        profileMyPostsRecyclerView.itemAnimator.changeDuration = 0
        profileMyPostsRecyclerView.addOnScrollListener(EndlessScrollListener({
            currentPage++
            fetchData(currentPage)
        }, linearLayoutManager))

        profileMyPostsRecyclerView.visibility = View.VISIBLE
    }

    private fun fetchData(currentPage: Int) {
        profileFragmentComponent.profilePresenter().loadSaleList(userTokenResponse.token, currentPage, Constants.RESULTS_PER_PAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                USER_EDIT_REQUEST -> {
                    userTokenResponse.user = data?.getParcelableExtra(USER_EXTRA)

                    userTokenResponse.user?.apply {
                        profileNameTextView.text = name
                        profileImageView.loadUrlToBeRounded(photoUri, R.drawable.ic_profile_placeholder)
                    }
                }
            }
        }
    }
}