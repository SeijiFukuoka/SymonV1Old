package br.com.symon.ui.retrievePassword

import android.content.Context
import android.content.Intent
import android.os.Bundle
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.common.isEmailValid
import br.com.symon.common.toast
import br.com.symon.injection.components.DaggerRetrieverPasswordActivityComponent
import br.com.symon.injection.components.RetrieverPasswordActivityComponent
import br.com.symon.injection.modules.RetrievePasswordActivityModule
import kotlinx.android.synthetic.main.activity_retrieve_password.*
import kotlinx.android.synthetic.main.view_custom_toolbar.*


class RetrieverPasswordActivity : BaseActivity(), RetrievePasswordContract.View {
    companion object {

        private val INTENT_EMAIL_EXTRA = "email_extra"

        lateinit var email: String

        fun newIntent(context: Context, email: String?): Intent {
            val intent = Intent(context, RetrieverPasswordActivity::class.java)
            intent.putExtra(INTENT_EMAIL_EXTRA, email)
            return intent
        }
    }

    private val retrieverPasswordActivityComponent: RetrieverPasswordActivityComponent
        get() = DaggerRetrieverPasswordActivityComponent
                .builder()
                .applicationComponent((this.application as CustomApplication).applicationComponent)
                .retrievePasswordActivityModule(RetrievePasswordActivityModule(this))
                .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrieve_password)

        retrieverPasswordActivityComponent.inject(this)

        setSupportActionBar(customToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        retrievePasswordHeaderProgressView.bind(2)

        RetrieverPasswordActivity.email = intent.extras.getString(RetrieverPasswordActivity.INTENT_EMAIL_EXTRA)
        retrievePasswordEmailEditText.setText(RetrieverPasswordActivity.email)

        customToolbarBackImageView.setOnClickListener {
            onBackPressed()
        }

        retrievePasswordEmailEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (!isEmailValid(retrievePasswordEmailEditText.text.toString())) {
                    retrievePasswordTextInputLayout.isErrorEnabled = true
                    retrievePasswordTextInputLayout.error = getString(R.string.register_email_invalid_msg)
                }
            } else {
                retrievePasswordTextInputLayout.isErrorEnabled = false
            }
        }

        retrievePasswordSendNewPasswordButton.setOnClickListener {
            if (isEmailValid(retrievePasswordEmailEditText.text.toString())) {
                retrievePasswordTextInputLayout.isErrorEnabled = false
                retrieverPasswordActivityComponent.retrievePasswordPresenter().requestNewPassword(retrievePasswordEmailEditText.text.toString())
            } else {
                retrievePasswordTextInputLayout.isErrorEnabled = true
                retrievePasswordTextInputLayout.error = getString(R.string.register_email_invalid_msg)
            }
        }
    }

    override fun goToNextStep() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showErrorMessage(message: String?) {
        toast(message)
    }

    override fun showError(message: Int) {
        toast(getString(message))
    }
}