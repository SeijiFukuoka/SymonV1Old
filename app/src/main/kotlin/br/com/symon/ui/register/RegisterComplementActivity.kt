package br.com.symon.ui.register

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.common.dateFormat
import br.com.symon.common.loadUrlToBeRounded
import br.com.symon.common.toast
import br.com.symon.data.model.User
import br.com.symon.data.model.requests.UserUpdateRequest
import br.com.symon.data.model.responses.UserTokenResponse
import br.com.symon.injection.components.DaggerRegisterComplementActivityComponent
import br.com.symon.injection.components.RegisterComplementActivityComponent
import br.com.symon.injection.modules.RegisterComplementActivityModule
import br.com.symon.ui.main.MainActivity
import com.github.vacxe.phonemask.PhoneMaskManager
import com.mlsdev.rximagepicker.RxImagePicker
import com.mlsdev.rximagepicker.Sources
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_register_complement.*
import kotlinx.android.synthetic.main.view_custom_toolbar.*
import java.util.*

class RegisterComplementActivity : BaseActivity(), RegisterComplementContract.View {
    companion object {
        private const val EXTRA_USER = "EXTRA_USER"

        private lateinit var user: User

        fun newIntent(context: Context, user: User?): Intent {
            val intent = Intent(context, RegisterComplementActivity::class.java)
            intent.putExtra(EXTRA_USER, user)
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

        user = intent.getParcelableExtra(EXTRA_USER)

        fillUserData()

        registerComplementHeaderProgressView.bind(2)

        customToolbarBackImageView.setOnClickListener {
            onBackPressed()
        }

        PhoneMaskManager()
                .withMask(getString(R.string.register_complement_phone_mask))
                .bindTo(registerComplementPhoneEditText)

        registerProfileImageView.setOnClickListener {
            RxPermissions(this)
                    .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe { granted ->
                        if (granted) {
                            pickImage()
                        } else {
                            toast(getString(R.string.general_permissions_message))
                        }
                    }

        }

        registerComplementBirthdayEditText.inputType = InputType.TYPE_NULL

        registerComplementBirthdayEditText.setOnClickListener {
            setupCalendar()
        }

        registerComplementBirthdayEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                setupCalendar()
            }
        }

        registerComplementContinueButton.setOnClickListener {
            if (registerComplementNameEditText.text.isEmpty()) {
                registerComplementNameTextInput.isErrorEnabled = true
                registerComplementNameTextInput.error = getString(R.string.general_required_field)
                return@setOnClickListener
            } else {
                registerComplementNameTextInput.isErrorEnabled = false
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
            }

            val userUpdateRequest = UserUpdateRequest(
                    name = registerComplementNameEditText.text.toString(),
                    phone = registerComplementPhoneEditText.text.toString(),
                    birthday = calendar.time
            )

            registerComplementComponent.registerComplementPresenter().updateUserInfo(
                    user.id!!,
                    userUpdateRequest
            )
        }
    }

    private fun fillUserData() {
        registerProfileImageView.loadUrlToBeRounded(user.photoUri)
        registerComplementNameEditText.setText(user.name)
    }

    override fun showPhoto(photo: String?) {
        photo?.let { registerProfileImageView.loadUrlToBeRounded(it) }
    }

    override fun goToMain(userTokenResponse: UserTokenResponse?) {
        val intent = MainActivity.newIntent(this, userTokenResponse)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun setupCalendar() {
        calendar = Calendar.getInstance()
        datePickerDialog = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
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

    private fun pickImage() {
        RxImagePicker.with(this).requestImage(Sources.GALLERY).subscribe { uri ->
            registerComplementComponent.registerComplementPresenter().uploadUserPhoto(user.id!!, uri)
        }
    }
}