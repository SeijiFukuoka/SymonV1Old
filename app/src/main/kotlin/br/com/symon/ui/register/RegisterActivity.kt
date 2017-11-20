package br.com.symon.ui.register

import android.os.Bundle
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.common.isEmailValid
import br.com.symon.common.toast
import br.com.symon.data.model.requests.UserAuthenticateRequest
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

        customToolbarBackImageView.setOnClickListener {
            onBackPressed()
        }

        registerEmailEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (!isEmailValid(registerEmailEditText.text.toString())) {
                    registerEmailTextInput.isErrorEnabled = true
                    registerEmailTextInput.error = getString(R.string.register_email_invalid_msg)
                }
            } else {
                registerEmailTextInput.isErrorEnabled = false
            }
        }

        registerContinueButton.setOnClickListener {

            if (isEmailValid(registerEmailEditText.text.toString())) {

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

    override fun goToNextStep(id: Int?) {
        startActivity(RegisterComplementActivity.newIntent(this, id))
        finish()
    }

    override fun showErrorMessage(message: String?) {
        toast(message)
    }

    override fun showError(message: Int) {
        toast(getString(message))
    }
}