package br.com.symon.ui.register

import android.os.Bundle
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.common.toast
import br.com.symon.data.model.requests.UserAuthenticateRequest
import br.com.symon.injection.components.DaggerRegisterComponent
import br.com.symon.injection.components.RegisterComponent
import br.com.symon.injection.modules.RegisterActivityModule
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern


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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showErrorMessage(message: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(message: Int) {
        toast(getString(message))
    }

    private fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }
}