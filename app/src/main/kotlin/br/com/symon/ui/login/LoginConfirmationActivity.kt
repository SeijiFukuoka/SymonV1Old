package br.com.symon.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import br.com.symon.R.layout
import br.com.symon.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login_confirmation.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class LoginConfirmationActivity : BaseActivity() {
    companion object {

        private val INTENT_EMAIL_EXTRA = "email_extra"

        lateinit var email: String

        fun newIntent(context: Context, email: String?): Intent {
            val intent = Intent(context, LoginConfirmationActivity::class.java)
            intent.putExtra(INTENT_EMAIL_EXTRA, email)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_login_confirmation)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        imageBackArrow.setOnClickListener {
            onBackPressed()
        }

        email = intent.extras.getString(INTENT_EMAIL_EXTRA)
        editTextLoginEmail.setText(email)


        editTextPassword.requestFocus()
    }
}