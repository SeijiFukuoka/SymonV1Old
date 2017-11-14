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

        buttonLogin.setOnClickListener({
            loginComponent.loginPresenter().checkUser(editTextLoginEmail.text.toString())
        })

        buttonRegister.setOnClickListener {
            Toast.makeText(this, "In Progress", Toast.LENGTH_SHORT).show()
        }
    }

    override fun handleCheckResponse(checkResponse: CheckUserResponse) {
        if (checkResponse.exists!!) {
            val recipeDetailActivity = LoginConfirmationActivity.newIntent(this,
                    editTextLoginEmail.text.toString())
            startActivity(recipeDetailActivity)
        } else {
            Toast.makeText(this, "usuário não encontrado", Toast.LENGTH_SHORT).show()
        }
    }
}