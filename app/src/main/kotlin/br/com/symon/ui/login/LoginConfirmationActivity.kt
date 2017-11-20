package br.com.symon.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import br.com.symon.CustomApplication
import br.com.symon.R.layout
import br.com.symon.base.BaseActivity
import br.com.symon.common.startIntent
import br.com.symon.data.model.requests.UserTokenRequest
import br.com.symon.data.model.responses.UserTokenResponse
import br.com.symon.injection.components.DaggerLoginConfirmationComponent
import br.com.symon.injection.components.LoginConfirmationComponent
import br.com.symon.injection.modules.LoginConfirmationModule
import br.com.symon.ui.MainActivity
import kotlinx.android.synthetic.main.activity_login_confirmation.*
import kotlinx.android.synthetic.main.layout_toolbar.*

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

    private val loginConfirmationComponent: LoginConfirmationComponent
        get() = DaggerLoginConfirmationComponent.builder()
                .applicationComponent((this.application as CustomApplication).applicationComponent)
                .loginConfirmationModule(LoginConfirmationModule(this))
                .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_login_confirmation)

        loginConfirmationComponent.inject(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        email = intent.extras.getString(INTENT_EMAIL_EXTRA)
        loginConfirmationEmailEditText.setText(email)
        loginConfirmationPasswordEditText.requestFocus()

        imageBackArrow.setOnClickListener {
            onBackPressed()
        }

        loginConfirmationConfirmLoginButton.setOnClickListener {
            val userTokenRequest = UserTokenRequest(email, loginConfirmationPasswordEditText.text.toString())
            loginConfirmationComponent.loginConfirmationPresenter().getUserToken(userTokenRequest)
        }

        loginConfirmationForgetPasswordTextButton.setOnClickListener {
            Toast.makeText(this, "Em progresso", Toast.LENGTH_SHORT).show()
        }
    }

    override fun handleTokenResponse(userTokenResponse: UserTokenResponse?) {
        startIntent(MainActivity::class.java)
    }

    override fun handleUserNotFoundResponse() {
        Toast.makeText(this, "usuário ou senha incorretos", Toast.LENGTH_SHORT).show()
    }
}