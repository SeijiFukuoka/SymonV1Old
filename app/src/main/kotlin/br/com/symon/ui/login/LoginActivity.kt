package br.com.symon.ui.login

import android.os.Bundle
import android.widget.Toast
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.common.isEmailValid
import br.com.symon.common.startIntent
import br.com.symon.data.model.responses.CheckUserResponse
import br.com.symon.injection.components.DaggerLoginActivityComponent
import br.com.symon.injection.components.LoginActivityComponent
import br.com.symon.injection.modules.LoginActivityModule
import br.com.symon.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.view_custom_toolbar.*

class LoginActivity : BaseActivity(), LoginContract.View {

    private val loginComponent: LoginActivityComponent
        get() = DaggerLoginActivityComponent.builder()
                .applicationComponent((this.application as CustomApplication).applicationComponent)
                .loginActivityModule(LoginActivityModule(this))
                .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginComponent.inject(this)

        setSupportActionBar(customToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        loginViewHeaderProgress.bind(1)

        customToolbarBackImageView.setOnClickListener {
            onBackPressed()
        }

        loginEmailEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (!isEmailValid(loginEmailEditText.text.toString())) {
                    loginEmailTextInputLayout.isErrorEnabled = true
                    loginEmailTextInputLayout.error = getString(R.string.login_email_invalid_msg)
                }
            } else {
                loginEmailTextInputLayout.isErrorEnabled = false
            }
        }

        loginDoLoginButton.setOnClickListener({
            if (isEmailValid(loginEmailEditText.text.toString())) {
                loginEmailTextInputLayout.isErrorEnabled = false

                loginComponent.loginPresenter().checkUser(loginEmailEditText.text.toString())
            } else {
                loginEmailTextInputLayout.isErrorEnabled = true
                loginEmailTextInputLayout.error = getString(R.string.login_email_invalid_msg)
            }
        })

        loginRegisterButton.setOnClickListener {
            startIntent(RegisterActivity::class.java)
        }
    }

    override fun handleCheckResponse(checkResponse: CheckUserResponse) {
        if (checkResponse.exists!!) {
            val loginConfirmationActivity = LoginConfirmationActivity.newIntent(this,
                    loginEmailEditText.text.toString())
            startActivity(loginConfirmationActivity)
        } else {
            Toast.makeText(this, "usuário não encontrado", Toast.LENGTH_SHORT).show()
        }
    }
}