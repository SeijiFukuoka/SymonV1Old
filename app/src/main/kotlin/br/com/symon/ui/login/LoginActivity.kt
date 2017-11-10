package br.com.symon.ui.login

import android.os.Bundle
import br.com.symon.R
import br.com.symon.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login.buttonLogin
import kotlinx.android.synthetic.main.activity_login.editTextLoginEmail
import kotlinx.android.synthetic.main.layout_toolbar.imageBackArrow
import kotlinx.android.synthetic.main.layout_toolbar.toolbar

class LoginActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayShowHomeEnabled(false)
    supportActionBar?.setDisplayShowTitleEnabled(false)

    imageBackArrow.setOnClickListener {
      onBackPressed()
    }

    buttonLogin.setOnClickListener({
      val recipeDetailActivity = LoginConfirmationActivity.newIntent(this,
          editTextLoginEmail.text.toString())
      startActivity(recipeDetailActivity)
    })
  }
}