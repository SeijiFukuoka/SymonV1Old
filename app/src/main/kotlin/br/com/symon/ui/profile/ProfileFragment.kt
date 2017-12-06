package br.com.symon.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.symon.R
import br.com.symon.base.BaseFragment
import br.com.symon.common.inflate
import br.com.symon.common.loadUrlToBeRounded
import br.com.symon.common.startIntent
import br.com.symon.data.model.User
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment: BaseFragment() {


    companion object {

        private val USER_EXTRA = "user_extra"

        fun newInstance(user: User?): ProfileFragment {
            val fragment = ProfileFragment()
            val args = Bundle()
            args.putParcelable(USER_EXTRA, user)
            fragment.arguments = args
            return fragment
        }
    }

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentId = ProfileFragment::class.java.canonicalName

        user = arguments.getParcelable(USER_EXTRA)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            container?.inflate(R.layout.fragment_profile)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user?.apply {
            profileNameTextView.text = name
            profileImageView.loadUrlToBeRounded(photoUri)
        }

        profileEditTextView.setOnClickListener {
            activity.startIntent(ProfileActivity::class.java)
        }

        profileMessageTextView.text = getString(R.string.profile_message)
    }
}