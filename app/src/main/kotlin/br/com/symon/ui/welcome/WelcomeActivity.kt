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
import br.com.symon.data.model.requests.UserFacebookAuthenticateRequest
import br.com.symon.data.model.responses.RegisterUserResponse
import br.com.symon.data.model.responses.UserTokenResponse
import br.com.symon.injection.components.DaggerWelcomeActivityComponent
import br.com.symon.injection.components.WelcomeActivityComponent
import br.com.symon.injection.modules.WelcomeActivityModule
import br.com.symon.ui.login.LoginActivity
import br.com.symon.ui.main.MainActivity
import br.com.symon.ui.register.RegisterComplementActivity
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

    private lateinit var userAuthenticate: UserFacebookAuthenticateRequest

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

            userAuthenticate = UserFacebookAuthenticateRequest(
                    name = name,
                    email = email,
                    facebookId = result?.accessToken?.userId)

            welcomeActivityComponent.welcomePresenter().getUserToken(userAuthenticate)
        }

        val parameters = Bundle()
        parameters.putString("fields", "${getString(R.string.facebook_email)},${getString(R.string.facebook_name)}")
        request.parameters = parameters
        request.executeAsync()
    }

    override fun handleTokenResponse(userTokenResponse: UserTokenResponse?) {
        startActivity(MainActivity.newIntent(this, userTokenResponse))
        finish()
    }

    override fun handleUserNotFoundResponse() {
        welcomeActivityComponent.welcomePresenter().register(userAuthenticate)
    }

    override fun goToNextStep(registerUserResponse: RegisterUserResponse?) {
        val user = User(id = registerUserResponse?.id,
                name = userAuthenticate.name,
                phone = "",
                email = userAuthenticate.email,
                birthday = null,
                facebookId = userAuthenticate.facebookId,
                photoUri = registerUserResponse?.photo)

        startActivity(RegisterComplementActivity.newIntent(this, user))
        finish()
    }

    override fun showErrorMessage(message: String?) {
        toast(message)
    }

    private fun facebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(
                getString(R.string.facebook_permission_profile),
                getString(R.string.facebook_permission_email)))
    }
}