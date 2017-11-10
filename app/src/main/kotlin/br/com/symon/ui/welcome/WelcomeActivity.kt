package br.com.symon.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.util.Log
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.common.toast
import br.com.symon.data.model.User
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.content_welcome.*
import java.util.*

class WelcomeActivity :
        BaseActivity(),
        WelcomeContract.View,
        FacebookCallback<LoginResult> {

    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        constraintLoginFacebookButtonContainer.setOnClickListener {
            facebookLogin()
        }

        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, this)
    }

    private fun facebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(
                getString(R.string.facebook_permission_profile),
                getString(R.string.facebook_permission_email)))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCancel() {
        Log.d("facebookEvent:", "Cancel")
    }

    override fun onError(error: FacebookException?) {
        Log.d("facebookEvent:", error?.message )
        toast(getString(R.string.facebook_error_message))
    }

    override fun onSuccess(result: LoginResult?) {
        Log.d("facebookEvent:", "Success")
        val request: GraphRequest = GraphRequest.newMeRequest(result?.accessToken) {
            `facebookObject`, _ ->
            val email = `facebookObject`.getString(getString(R.string.facebook_email))
            val name = `facebookObject`.getString(getString(R.string.facebook_name))

            var user = User(
                    id = null,
                    name =  name,
                    email = email,
                    phone = "",
                    birthday = null,
                    facebookId = result?.accessToken?.userId,
                    photo = "https://graph.facebook.com/${result?.accessToken?.userId}/picture?type=large")
        }

        val parameters = Bundle()
        parameters.putString("fields", "${getString(R.string.facebook_email)},${getString(R.string.facebook_name)}")
        request.parameters = parameters
        request.executeAsync()
    }
}