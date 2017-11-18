package br.com.symon.ui.login

import android.os.Bundle
import android.widget.Toast
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.data.model.responses.CheckUserResponse
import br.com.symon.injection.components.DaggerLoginComponent
import br.com.symon.injection.components.LoginComponent
import br.com.symon.injection.modules.LoginModule
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.util.regex.Pattern

class LoginActivity : BaseActivity(), LoginContract.View {

    private val loginComponent: LoginComponent
        get() = DaggerLoginComponent.builder()
                .applicationComponent((this.application as CustomApplication).applicationComponent)
                .loginModule(LoginModule(this))
                .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginComponent.inject(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        imageBackArrow.setOnClickListener {
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
            Toast.makeText(this, "In Progress", Toast.LENGTH_SHORT).show()
        }
    }

    override fun handleCheckResponse(checkResponse: CheckUserResponse) {
        if (checkResponse.exists!!) {
            val recipeDetailActivity = LoginConfirmationActivity.newIntent(this,
                    loginEmailEditText.text.toString())
            startActivity(recipeDetailActivity)
        } else {
            Toast.makeText(this, "usuário não encontrado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }
}