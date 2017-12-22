package br.com.symon.ui.register

import android.os.Bundle
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.common.isEmailValid
import br.com.symon.common.toast
import br.com.symon.data.model.User
import br.com.symon.data.model.requests.UserAuthenticateRequest
import br.com.symon.data.model.responses.RegisterUserResponse
import br.com.symon.injection.components.DaggerRegisterComponent
import br.com.symon.injection.components.RegisterComponent
import br.com.symon.injection.modules.RegisterActivityModule
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.view_custom_toolbar.*


class RegisterActivity : BaseActivity(), RegisterContract.View {

    private val registerComponent: RegisterComponent
        get() = DaggerRegisterComponent
                .builder()
                .applicationComponent((this.application as CustomApplication).applicationComponent)
                .registerActivityModule(RegisterActivityModule(this))
                .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerComponent.inject(this)

        setSupportActionBar(customToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        registerHeaderProgressView.bind(1)

        customToolbarBackImageView.setOnClickListener {
            onBackPressed()
        }

        registerEmailEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (!registerEmailEditText.text.toString().isEmailValid()) {
                    registerEmailTextInput.isErrorEnabled = true
                    registerEmailTextInput.error = getString(R.string.register_email_invalid_msg)
                }
            } else {
                registerEmailTextInput.isErrorEnabled = false
            }
        }

        registerContinueButton.setOnClickListener {

            if (registerEmailEditText.text.toString().isEmailValid()) {

                registerEmailTextInput.isErrorEnabled = false

                val userAuthenticate = UserAuthenticateRequest(
                        email = registerEmailEditText.text.toString(),
                        password = registerPasswordEditText.text.toString())

                registerComponent.registerPresenter().register(userAuthenticate)
            } else {
                registerEmailTextInput.isErrorEnabled = true
                registerEmailTextInput.error = getString(R.string.register_email_invalid_msg)
            }
        }
    }

    override fun goToNextStep(registerUserResponse: RegisterUserResponse?) {
        val user = User(id = registerUserResponse?.id,
                name = "",
                phone = "",
                email = "",
                birthday = null,
                facebookId = "",
                photoUri = registerUserResponse?.photo)

        startActivity(RegisterComplementActivity.newIntent(this, user))
        finish()
    }

    override fun showErrorMessage(message: String?) {
        toast(message)
    }

    override fun showError(message: Int) {
        toast(getString(message))
    }
}