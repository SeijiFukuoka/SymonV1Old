package br.com.symon.ui.login

import android.os.Bundle
import br.com.symon.R
import br.com.symon.base.BaseActivity

class LoginActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    showLoading()
  }
}