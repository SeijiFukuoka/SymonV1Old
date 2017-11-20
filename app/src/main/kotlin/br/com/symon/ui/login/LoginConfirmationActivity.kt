package br.com.symon.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.R.layout
import br.com.symon.base.BaseActivity
import br.com.symon.common.toast
import br.com.symon.data.model.requests.UserAuthenticateRequest
import br.com.symon.data.model.responses.UserTokenResponse
import br.com.symon.injection.components.DaggerLoginActivityConfirmationComponent
import br.com.symon.injection.components.LoginActivityConfirmationComponent
import br.com.symon.injection.modules.LoginActivityConfirmationModule
import br.com.symon.ui.MainActivity
import kotlinx.android.synthetic.main.activity_login_confirmation.*
import kotlinx.android.synthetic.main.view_custom_toolbar.*

class LoginConfirmationActivity : BaseActivity(), LoginConfirmationContract.View {
    companion object {

        private val INTENT_EMAIL_EXTRA = "email_extra"

        lateinit var email: String

        fun newIntent(context: Context, email: String?): Intent {
            val intent = Intent(context, LoginConfirmationActivity::class.java)
            intent.putExtra(INTENT_EMAIL_EXTRA, email)
            return intent
        }
    }

    private val loginActivityConfirmationComponent: LoginActivityConfirmationComponent
        get() = DaggerLoginActivityConfirmationComponent.builder()
                .applicationComponent((this.application as CustomApplication).applicationComponent)
                .loginActivityConfirmationModule(LoginActivityConfirmationModule(this))
                .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_login_confirmation)

        loginActivityConfirmationComponent.inject(this)

        setSupportActionBar(customToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        email = intent.extras.getString(INTENT_EMAIL_EXTRA)
        loginConfirmationEmailEditText.setText(email)
        loginConfirmationPasswordEditText.requestFocus()

        customToolbarBackImageView.setOnClickListener {
            onBackPressed()
        }

        loginConfirmationConfirmLoginButton.setOnClickListener {
            val userTokenRequest = UserAuthenticateRequest(email, loginConfirmationPasswordEditText.text.toString())
            loginActivityConfirmationComponent.loginConfirmationPresenter().getUserToken(userTokenRequest)
        }

        loginConfirmationForgetPasswordTextButton.setOnClickListener {
            Toast.makeText(this, "Em progresso", Toast.LENGTH_SHORT).show()
        }
    }

    override fun handleTokenResponse(userTokenResponse: UserTokenResponse?) {
        val intent = MainActivity.newIntent(this, userTokenResponse)
        startActivity(intent)
        finish()
    }

    override fun handleUserNotFoundResponse() {
        toast(getString(R.string.login_invalid_message))
    }
}