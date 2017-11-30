package br.com.symon.ui.profile

import android.os.Bundle
import br.com.symon.R
import br.com.symon.base.BaseActivity
import kotlinx.android.synthetic.main.view_custom_toolbar.*


class ProfileActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setSupportActionBar(customToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        customToolbarTitleTextView.text = getString(R.string.profile_edit_label)

        customToolbarBackImageView.setOnClickListener {
            onBackPressed()
        }

    }

}