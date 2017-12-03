package br.com.symon.ui.profile

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.common.dateFormat
import br.com.symon.common.hideKeyboard
import br.com.symon.common.loadUrlWithCircularImage
import br.com.symon.common.toast
import br.com.symon.data.model.User
import br.com.symon.data.model.requests.UserUpdateRequest
import br.com.symon.injection.components.DaggerProfileActivityComponent
import br.com.symon.injection.components.ProfileActivityComponent
import br.com.symon.injection.modules.ProfileActivityModule
import com.github.vacxe.phonemask.PhoneMaskManager
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_register_complement.*
import kotlinx.android.synthetic.main.view_custom_toolbar.*
import java.util.*


class ProfileActivity : BaseActivity(), ProfileContract.View {

    companion object {
        private val REQUEST_PICK_IMAGE = 10011
    }

    private val profileActivityComponent: ProfileActivityComponent
        get() = DaggerProfileActivityComponent
                .builder()
                .applicationComponent((application as CustomApplication).applicationComponent)
                .profileActivityModule(ProfileActivityModule(this))
                .build()

    private var isPasswordChange = false
    private var user: User? = null
    private lateinit var calendar: Calendar
    private lateinit var datePickerDialog: DatePickerDialog.OnDateSetListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileActivityComponent.inject(this)

        setSupportActionBar(customToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        customToolbarTitleTextView.text = getString(R.string.profile_edit_label)

        customToolbarBackImageView.setOnClickListener {
            onBackPressed()
        }

        PhoneMaskManager()
                .withMask(getString(R.string.register_complement_phone_mask))
                .bindTo(profilePhoneEditText)

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

        profileBirthdayEditText.setOnClickListener {
            setupCalendar()
        }

        profileBirthdayEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                setupCalendar()
            }
        }

        profileChangePasswordTextView.setOnClickListener {
            if (!isPasswordChange) {
                profilePasswordContainerLinearLayout.visibility = View.VISIBLE
                profileChangePasswordTextView.text = getString(R.string.general_cancel)
            } else {
                profilePasswordContainerLinearLayout.visibility = View.GONE
                profileChangePasswordTextView.text = getString(R.string.profile_change_password_label)
            }

            val content = SpannableString(profileChangePasswordTextView.text)
            content.setSpan(UnderlineSpan(), 0, content.length, 0)
            profileChangePasswordTextView.text = content

            isPasswordChange = !isPasswordChange
        }

        profileContinueButton.setOnClickListener {
            if (isValidUserData() && isValidUserPassword()) {

                var userUpdateRequest = if (!isPasswordChange) {
                    UserUpdateRequest(
                            name = profileNameEditText.text.toString(),
                            phone = profilePhoneEditText.text.toString(),
                            birthday = calendar.time)
                } else {
                    UserUpdateRequest(
                            name = profileNameEditText.text.toString(),
                            phone = profilePhoneEditText.text.toString(),
                            birthday = calendar.time,
                            currentPassword =  profilePasswordEditText.text.toString(),
                            newPassword = profileNewPasswordEditText.text.toString())
                }

                profileActivityComponent.profilePresenter().updateUserInfo(user?.id!!,
                        userUpdateRequest)
            }
        }

        profileActivityComponent.profilePresenter().getUserCache()
    }

    override fun notifyDataUpdate() {
        toast(getString(R.string.profile_data_updated_success))
    }

    override fun showPhoto(photo: String?) {
        profileImageView.loadUrlWithCircularImage(photo, R.drawable.ic_profile_placeholder)
    }

    override fun showUserData(user: User?) {
        this.user = user
        user?.apply {
            profileImageView.loadUrlWithCircularImage(photoUri, R.drawable.ic_profile_placeholder)
            profileNameEditText.setText(name)
            profileEmailEditText.setText(email)
            profilePhoneEditText.setText(phone)
            profileBirthdayEditText.setText(birthday?.dateFormat())
        }
    }

    private fun isValidUserPassword(): Boolean {
        if (!isPasswordChange) {
            return true
        }

        if (profilePasswordEditText.text.isEmpty()) {
            profilePasswordTextInput.isErrorEnabled = true
            profilePasswordTextInput.error = getString(R.string.general_required_field)
            return false
        } else {
            profilePasswordTextInput.isErrorEnabled = false
        }

        if (profileNewPasswordEditText.text.isEmpty()) {
            profileNewPasswordTextInput.isErrorEnabled = true
            profileNewPasswordTextInput.error = getString(R.string.general_required_field)
            return false
        } else {
            profileNewPasswordTextInput.isErrorEnabled = false
        }

        if (profileConfirmNewPasswordEditText.text.isEmpty()) {
            profileConfirmNewPasswordTextInput.isErrorEnabled = true
            profileConfirmNewPasswordTextInput.error = getString(R.string.general_required_field)
            return false
        } else {
            profileConfirmNewPasswordTextInput.isErrorEnabled = false
        }

        if (profileNewPasswordEditText.text !== profileConfirmNewPasswordEditText.text) {
            profileConfirmNewPasswordTextInput.isErrorEnabled = true
            profileConfirmNewPasswordTextInput.error = getString(R.string.profile_password_not_match)
            return false
        } else {
            profileConfirmNewPasswordTextInput.isErrorEnabled = false
        }

        return true
    }

    private fun isValidUserData(): Boolean {
        if (profileNameEditText.text.isEmpty()) {
            profileNameTextInput.isErrorEnabled = true
            profileNameTextInput.error = getString(R.string.general_required_field)
            return false
        } else {
            profileNameTextInput.isErrorEnabled = false
        }

        if (profileBirthdayEditText.text.isEmpty()) {
            profileBirthdayTextInput.isErrorEnabled = true
            profileBirthdayTextInput.error = getString(R.string.general_required_field)
            return false
        } else {
            profileBirthdayTextInput.isErrorEnabled = false
        }

        if (profilePhoneEditText.text.isEmpty()) {
            profilePhoneTextInput.isErrorEnabled = true
            profilePhoneTextInput.error = getString(R.string.general_required_field)
            return false
        } else {
            profilePhoneTextInput.isErrorEnabled = false
        }

        return true
    }

    private fun setupCalendar() {
        profileBirthdayEditText.hideKeyboard()

        calendar = Calendar.getInstance()
        datePickerDialog = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            profileBirthdayEditText.setText(calendar.time?.dateFormat())
        }

        DatePickerDialog(this, datePickerDialog,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun pickImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            startActivityForResult(Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                    REQUEST_PICK_IMAGE)
        } else {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_PICK_IMAGE)
        }
    }
}