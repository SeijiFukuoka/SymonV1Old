package br.com.symon.ui.profile

import android.os.Bundle
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.common.dateFormat
import br.com.symon.common.loadUrlWithCircularImage
import br.com.symon.data.model.User
import br.com.symon.injection.components.DaggerProfileActivityComponent
import br.com.symon.injection.components.ProfileActivityComponent
import br.com.symon.injection.modules.ProfileActivityModule
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.view_custom_toolbar.*


class ProfileActivity : BaseActivity(), ProfileContract.View {

    private val profileActivityComponent: ProfileActivityComponent
    get() = DaggerProfileActivityComponent
            .builder()
            .applicationComponent((application as CustomApplication).applicationComponent)
            .profileActivityModule(ProfileActivityModule(this))
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileActivityComponent.inject(this)

        setSupportActionBar(customToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        customToolbarTitleTextView.text = getString(R.string.profile_edit_label)

        customToolbarBackImageView.setOnClickListener {
            onBackPressed()
        }


        profileActivityComponent.profilePresenter().getUserCache()
    }

    override fun showUserData(user: User?) {
        user?.apply {
            profileImageView.loadUrlWithCircularImage(photoUri, R.drawable.ic_profile_placeholder)
            profileNameEditText.setText(name)
            profileEmailEditText.setText(email)
            profilePhoneEditText.setText(phone)
            profileBirthdayEditText.setText(birthday?.dateFormat())
        }
    }

}