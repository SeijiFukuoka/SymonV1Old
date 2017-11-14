package br.com.symon.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import br.com.symon.CustomApplication
import br.com.symon.R.layout
import br.com.symon.base.BaseActivity
import br.com.symon.data.model.UserTokenRequest
import br.com.symon.data.model.UserTokenResponse
import br.com.symon.injection.components.DaggerLoginConfirmationComponent
import br.com.symon.injection.components.LoginConfirmationComponent
import br.com.symon.injection.modules.LoginConfirmationModule
import kotlinx.android.synthetic.main.activity_login_confirmation.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import javax.inject.Inject

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

    val loginConfirmationComponent: LoginConfirmationComponent
        get() = DaggerLoginConfirmationComponent.builder()
                .applicationComponent((this.application as CustomApplication).applicationComponent)
                .loginConfirmationModule(LoginConfirmationModule(this))
                .build()

    @Inject
    lateinit var loginConfirmationPresenter: LoginConfirmationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_login_confirmation)

        loginConfirmationComponent.inject(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        email = intent.extras.getString(INTENT_EMAIL_EXTRA)
        editTextLoginEmail.setText(email)
        editTextPassword.requestFocus()

        imageBackArrow.setOnClickListener {
            onBackPressed()
        }

        buttonConfirmLogin.setOnClickListener {
            val userTokenRequest = UserTokenRequest(email, editTextPassword.text.toString())
            loginConfirmationPresenter.getUserToken(userTokenRequest)
        }

        textButtonForgetPassword.setOnClickListener {
            Toast.makeText(this, "Em progresso", Toast.LENGTH_SHORT).show()
        }
    }

    override fun handleTokenResponse(userTokenResponse: UserTokenResponse) {
        Toast.makeText(this, "Login de ${userTokenResponse.user?.name} realizado com sucesso", Toast.LENGTH_SHORT).show()
    }

    override fun handleUserNotFoundResponse() {
        Toast.makeText(this, "usuário ou senha incorretos", Toast.LENGTH_SHORT).show()
    }
}