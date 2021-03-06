package br.com.symon.ui.editProfile

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.common.dateFormat
import br.com.symon.common.loadUrlToBeRounded
import br.com.symon.common.toast
import br.com.symon.data.model.User
import br.com.symon.data.model.requests.UserFullUpdateRequest
import br.com.symon.injection.components.DaggerEditProfileActivityComponent
import br.com.symon.injection.components.EditProfileActivityComponent
import br.com.symon.injection.modules.EditProfileActivityModule
import br.com.symon.ui.profile.ProfileFragment
import br.com.symon.ui.welcome.WelcomeActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.github.vacxe.phonemask.PhoneMaskManager
import com.mlsdev.rximagepicker.RxImagePicker
import com.mlsdev.rximagepicker.Sources
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.view_custom_toolbar.*
import java.util.*


class EditProfileActivity : BaseActivity(),
        EditProfileContract.View,
        FacebookCallback<LoginResult> {

    private val editProfileActivityComponent: EditProfileActivityComponent
        get() = DaggerEditProfileActivityComponent
                .builder()
                .applicationComponent((application as CustomApplication).applicationComponent)
                .editProfileActivityModule(EditProfileActivityModule(this))
                .build()

    private var isPasswordChange = false
    private var user: User? = null
    private var facebookId: String? = null

    private lateinit var calendar: Calendar
    private lateinit var datePickerDialog: DatePickerDialog.OnDateSetListener
    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        editProfileActivityComponent.inject(this)

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

        profileImageView.setOnClickListener {
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

        profileBirthdayEditText.inputType = InputType.TYPE_NULL

        profileBirthdayEditText.setOnClickListener {
            setupCalendar()
        }

        profileBirthdayEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                setupCalendar()
            }
        }

        profileLogoutButton.setOnClickListener {
            editProfileActivityComponent.profilePresenter().deleteUserCache()
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

                val userUpdateRequest = if (!isPasswordChange) {
                    UserFullUpdateRequest(
                            name = profileNameEditText.text.toString(),
                            phone = profilePhoneEditText.text.toString(),
                            birthday = calendar.time,
                            email = profileEmailEditText.text.toString(),
                            facebookId = facebookId)
                } else {
                    UserFullUpdateRequest(
                            name = profileNameEditText.text.toString(),
                            phone = profilePhoneEditText.text.toString(),
                            birthday = calendar.time,
                            email = profileEmailEditText.text.toString(),
                            facebookId = facebookId,
                            currentPassword = profilePasswordEditText.text.toString(),
                            newPassword = profileNewPasswordEditText.text.toString())
                }

                user?.apply {
                    id?.let {
                        editProfileActivityComponent.profilePresenter().updateUserInfo(it, userUpdateRequest)
                    }
                }
            }
        }

        editProfileActivityComponent.profilePresenter().getUserCache()
    }

    override fun onBackPressed() {
        val returnIntent = Intent()
        setResult(Activity.RESULT_CANCELED, returnIntent)
        super.onBackPressed()
    }

    override fun logout() {
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    private fun setupFacebookButton() {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, this)

        profileFacebookTextView.text = if (verifyFacebookToken()) {
            getString(R.string.profile_facebook_disconnect)
        } else {
            getString(R.string.profile_facebook_connect)
        }

        profileFacebookButtonContainerConstraint.setOnClickListener {
            if (verifyFacebookToken()) {
                facebookLogout()
            } else {
                facebookLogin()
            }
        }
    }

    override fun notifyDataUpdate(user: User?) {
        toast(getString(R.string.profile_data_updated_success))
        val returnIntent = Intent()
        returnIntent.putExtra(ProfileFragment.USER_EXTRA, user)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun showErrorMessage(error: String?) {
        toast(error)
    }

    override fun showPhoto(photo: String?) {
        profileImageView.loadUrlToBeRounded(photo, R.drawable.ic_profile_placeholder)
    }

    override fun showUserData(user: User?) {
        this.user = user
        user?.apply {
            profileImageView.loadUrlToBeRounded(photoUri, R.drawable.ic_profile_placeholder)
            profileNameEditText.setText(name)
            profileEmailEditText.setText(email)
            profilePhoneEditText.setText(phone)
            profileBirthdayEditText.setText(birthday?.dateFormat())

            birthday?.let {
                calendar = Calendar.getInstance()
                calendar.time = it
            }
        }

        setupFacebookButton()
    }

    override fun showInvalidPassword() {
        profilePasswordEditText.setText("")
        profileNewPasswordTextInput.requestFocus()
        profilePasswordTextInput.isErrorEnabled = true
        profilePasswordTextInput.error = getString(R.string.profile_wrong_password)
    }

    private fun verifyFacebookToken(): Boolean {
        return !user?.facebookId.isNullOrEmpty() && AccessToken.getCurrentAccessToken() != null
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
        val request: GraphRequest = GraphRequest.newMeRequest(result?.accessToken) { _, _ ->
            facebookId = result?.accessToken?.userId
            profileFacebookTextView.text = getString(R.string.profile_facebook_disconnect)
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

    private fun facebookLogout() {
        GraphRequest(AccessToken.getCurrentAccessToken(),
                getString(R.string.facebook_permissions),
                null,
                HttpMethod.DELETE,
                GraphRequest.Callback {
                }).executeAsync()
        LoginManager.getInstance().logOut()
        profileFacebookTextView.text = getString(R.string.profile_facebook_connect)
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

        if (profileNewPasswordEditText.text.toString() == profileConfirmNewPasswordEditText.text.toString()) {
            profileConfirmNewPasswordTextInput.isErrorEnabled = false
        } else {
            profileConfirmNewPasswordTextInput.isErrorEnabled = true
            profileConfirmNewPasswordTextInput.error = getString(R.string.profile_password_not_match)
            return false
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
        RxImagePicker.with(this).requestImage(Sources.GALLERY).subscribe { uri ->
            user?.apply {
                id?.let {
                    editProfileActivityComponent.profilePresenter().uploadUserPhoto(it, uri)
                }
            }
        }
    }
}