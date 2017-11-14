package br.com.symon.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.common.startIntent
import br.com.symon.data.model.User
import br.com.symon.injection.components.DaggerSplashActivityComponent
import br.com.symon.injection.components.SplashActivityComponent
import br.com.symon.injection.modules.SplashActivityModule
import br.com.symon.ui.MainActivity
import br.com.symon.ui.splash.SplashContract.View
import br.com.symon.ui.welcome.WelcomeActivity

class SplashActivity : AppCompatActivity(), View {

    private val splashActivityComponent: SplashActivityComponent
        get() = DaggerSplashActivityComponent
                .builder()
                .applicationComponent((this.application as CustomApplication).applicationComponent)
                .splashActivityModule(SplashActivityModule(this))
                .build()

    companion object {
        private var SPLASH_TIME_OUT = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashActivityComponent.inject(this)

        Handler().postDelayed(
                {
                    splashActivityComponent.splashPresenter().getUserCache()
                }, SPLASH_TIME_OUT.toLong())
    }

    override fun setupNavigation(user: User) {
        if (user.id == null) {
            startIntent(WelcomeActivity::class.java)
        } else {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(MainActivity.EXTRA_USER, user)
            startActivity(intent)
        }

        finish()
    }

}