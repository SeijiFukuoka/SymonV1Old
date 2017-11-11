package br.com.symon.ui.login

import android.os.Bundle
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.data.model.User
import br.com.symon.injection.components.DaggerLoginComponent
import br.com.symon.injection.components.LoginComponent
import br.com.symon.injection.modules.LoginModule
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import javax.inject.Inject

class LoginActivity : BaseActivity(), LoginContract.View {

    val loginComponent: LoginComponent
        get() = DaggerLoginComponent.builder()
                .applicationComponent((this.application as CustomApplication).applicationComponent)
                .loginModule(LoginModule(this))
                .build()

    private var user: User? = null

    @Inject
    lateinit var loginPresenter: LoginPresenter

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
            // TODO("Verificar como vai ser esta chamada da API")
            user = User(1, "teste", "teste@teste.com", null, null, null, null)
            loginPresenter.login(user!!)
        })
    }

    override fun showLoginResponse(user: User) {
        val recipeDetailActivity = LoginConfirmationActivity.newIntent(this,
                editTextLoginEmail.text.toString())
        startActivity(recipeDetailActivity)
    }
}