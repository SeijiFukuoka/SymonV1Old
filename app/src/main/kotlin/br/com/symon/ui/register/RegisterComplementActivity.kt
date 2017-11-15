package br.com.symon.ui.register

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.common.dateFormat
import br.com.symon.common.hideKeyboard
import br.com.symon.common.startIntent
import br.com.symon.data.model.requests.UserUpdateRequest
import br.com.symon.injection.components.DaggerRegisterComplementActivityComponent
import br.com.symon.injection.components.RegisterComplementActivityComponent
import br.com.symon.injection.modules.RegisterComplementActivityModule
import br.com.symon.ui.MainActivity
import com.github.vacxe.phonemask.PhoneMaskManager
import kotlinx.android.synthetic.main.activity_register_complement.*
import kotlinx.android.synthetic.main.view_custom_toolbar.*
import java.util.*


class RegisterComplementActivity : BaseActivity(), RegisterComplementContract.View {
    companion object {
        private const val EXTRA_USER_ID = "EXTRA_USER_ID"

        var userId: Int = 0

        fun newIntent(context: Context, userId: Int?): Intent {
            val intent = Intent(context, RegisterComplementActivity::class.java)
            intent.putExtra(EXTRA_USER_ID, userId)
            return intent
        }
    }

    private val registerComplementComponent: RegisterComplementActivityComponent
    get() = DaggerRegisterComplementActivityComponent
            .builder()
            .applicationComponent((this.application as CustomApplication).applicationComponent)
            .registerComplementActivityModule(RegisterComplementActivityModule(this))
            .build()

    private lateinit var calendar: Calendar
    private lateinit var datePickerDialog: DatePickerDialog.OnDateSetListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_complement)

        setSupportActionBar(customToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        registerComplementComponent.inject(this)

        userId = intent.getIntExtra(EXTRA_USER_ID, 0)

        customToolbarBackImageView.setOnClickListener {
            onBackPressed()
        }

        PhoneMaskManager()
                .withMask(getString(R.string.register_complement_phone_mask))
                .bindTo(registerComplementPhoneEditText)

        registerComplementBirthdayEditText.setOnClickListener {
            setupCalendar()
        }

        registerComplementBirthdayEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                setupCalendar()
            }
        }

        registerComplementContinueButton.setOnClickListener {
           /* if (registerComplementNameEditText.text.isEmpty()) {
                registerComplementNameTextInput.isErrorEnabled = true
                registerComplementNameTextInput.error = getString(R.string.general_required_field)
                return@setOnClickListener
            } else {
                registerComplementPhoneTextInput.isErrorEnabled = false
            }

            if (registerComplementBirthdayEditText.text.isEmpty()) {
                registerComplementBirthdayTextInput.isErrorEnabled = true
                registerComplementBirthdayTextInput.error = getString(R.string.general_required_field)
                return@setOnClickListener
            } else {
                registerComplementBirthdayTextInput.isErrorEnabled = false
            }

            if (registerComplementPhoneEditText.text.isEmpty()) {
                registerComplementPhoneTextInput.isErrorEnabled = true
                registerComplementPhoneTextInput.error = getString(R.string.general_required_field)
                return@setOnClickListener
            } else {
                registerComplementPhoneTextInput.isErrorEnabled = false
            }*/

            val userUpdateRequest = UserUpdateRequest(
                    name = registerComplementNameEditText.text.toString(),
                    phone = registerComplementPhoneEditText.text.toString(),
                    birthday = calendar.time
            )

            registerComplementComponent.registerComplementPresenter().updateUserInfo(
                    userId,
                    userUpdateRequest
            )
        }
    }

    override fun goToMain() {
        startIntent(MainActivity::class.java)
    }

    private fun setupCalendar() {
        registerComplementBirthdayEditText.hideKeyboard()

        calendar = Calendar.getInstance()
        datePickerDialog = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            registerComplementBirthdayEditText.setText(calendar.time?.dateFormat())
        }

        DatePickerDialog(this, datePickerDialog,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
    }
}