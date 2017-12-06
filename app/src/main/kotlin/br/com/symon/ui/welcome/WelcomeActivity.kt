package br.com.symon.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.util.Log
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.common.startIntent
import br.com.symon.common.toast
import br.com.symon.data.model.User
import br.com.symon.data.model.requests.UserFacebookRegistryRequest
import br.com.symon.injection.components.DaggerWelcomeActivityComponent
import br.com.symon.injection.components.WelcomeActivityComponent
import br.com.symon.injection.modules.WelcomeActivityModule
import br.com.symon.ui.MainActivity
import br.com.symon.ui.login.LoginActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_welcome.*
import java.util.*

class WelcomeActivity :
        BaseActivity(),
        WelcomeContract.View,
        FacebookCallback<LoginResult> {

    private val welcomeActivityComponent: WelcomeActivityComponent
        get() = DaggerWelcomeActivityComponent
                .builder()
                .applicationComponent((this.application as CustomApplication).applicationComponent)
                .welcomeActivityModule(WelcomeActivityModule(this))
                .build()

    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        welcomeActivityComponent.inject(this)

        constraintLoginFacebookButtonContainer.setOnClickListener {
            facebookLogin()
        }

        welcomeContinueWithEmailButton.setOnClickListener { startIntent(LoginActivity::class.java) }

        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, this)
    }

    override fun redirectMainActivity(user: User) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_USER, user)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCancel() {
        Log.d("facebookEvent:", "Cancel")
    }

    override fun onError(error: FacebookException?) {
        Log.d("facebookEvent:", error?.message)
        toast(getString(R.string.facebook_error_message))
    }

    override fun onSuccess(result: LoginResult?) {
        Log.d("facebookEvent:", "Success")
        val request: GraphRequest = GraphRequest.newMeRequest(result?.accessToken) { jsonObject, _ ->
            val email = jsonObject.getString(getString(R.string.facebook_email))
            val name = jsonObject.getString(getString(R.string.facebook_name))

            val user = UserFacebookRegistryRequest(
                    name = name,
                    email = email,
                    facebookId = result?.accessToken?.userId)

            welcomeActivityComponent.welcomePresenter().registerUserFacebook(user)
        }

        val parameters = Bundle()
        parameters.putString("fields", "${getString(R.string.facebook_email)},${getString(R.string.facebook_name)}")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun facebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(
                getString(R.string.facebook_permission_profile),
                getString(R.string.facebook_permission_email)))
    }
}