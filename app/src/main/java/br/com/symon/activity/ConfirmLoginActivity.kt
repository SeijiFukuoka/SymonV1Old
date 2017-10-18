package br.com.symon.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.app.LoaderManager.LoaderCallbacks
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView

import android.Manifest.permission.READ_CONTACTS
import android.graphics.Typeface
import br.com.symon.R

import kotlinx.android.synthetic.main.activity_confirm_login.*
import kotlinx.android.synthetic.main.activity_sign.*
import java.util.*

/**
 * A login screen that offers login via email/password.
 */
class ConfirmLoginActivity : AppCompatActivity() {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private var mAuthTask: UserLoginTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_confirm_login)

        val regulartTypeface = Typeface.createFromAsset(assets,
                String.format(Locale.US, "fonts/%s", "Montserrat-Regular.ttf"))
        password.typeface = regulartTypeface

        val boldTypeface = Typeface.createFromAsset(assets,
                String.format(Locale.US, "fonts/%s", "Montserrat-Bold.ttf"))
        confirm_email.typeface = boldTypeface
        agora_sua_senha.typeface = boldTypeface

        val mediumTypeface = Typeface.createFromAsset(assets,
                String.format(Locale.US, "fonts/%s", "Montserrat-Medium.ttf"))
        forgot_password.typeface = mediumTypeface
        confirm_login_button.setAllCaps(false)
        confirm_login_button.typeface = mediumTypeface
        confirm_login_button.setShadowLayer(0.01f, -2.toFloat(), 2.toFloat(), getResources().getColor(R.color.blackAlpha));
        confirm_login_button.setOnClickListener { attemptLogin() }

        back_button.setOnClickListener { back() }
    }

    private fun back() {
        finish()
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        if (mAuthTask != null) {
            return
        }

        // Reset errors.
        email.error = null

        // Store values at the time of the login attempt.
        val emailStr = email.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            email.error = getString(R.string.error_field_required)
            focusView = email
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            email.error = getString(R.string.error_invalid_email)
            focusView = email
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)
            mAuthTask = UserLoginTask(emailStr)
            mAuthTask!!.execute(null as Void?)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.contains("@")
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        confirm_login_form.visibility = if (show) View.GONE else View.VISIBLE
        confirm_login_form.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        confirm_login_form.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

        confirm_login_progress.visibility = if (show) View.VISIBLE else View.GONE
        confirm_login_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        confirm_login_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })

    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    inner class UserLoginTask internal constructor(private val mEmail: String) : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg params: Void): Boolean? {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000)
            } catch (e: InterruptedException) {
                return false
            }

            return DUMMY_CREDENTIALS
                    .map { it.split(":") }
                    .firstOrNull { it[0] == mEmail }
                    ?.let {
                        // Account exists, return true if the password matches.
                        true
                    }
                    ?: true
        }

        override fun onPostExecute(success: Boolean?) {
            mAuthTask = null
            showProgress(false)

            if (success!!) {
                finish()
            } else {

            }
        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)
        }
    }

    companion object {

        /**
         * A dummy authentication store containing known user names and passwords.
         * TODO: remove after connecting to a real authentication system.
         */
        private val DUMMY_CREDENTIALS = arrayOf("foo@example.com:hello", "bar@example.com:world")
    }
}
