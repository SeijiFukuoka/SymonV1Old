package br.com.symon.ui.send

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.common.loadUrl
import br.com.symon.common.toast
import br.com.symon.common.widget.MoneyTextWatcher
import br.com.symon.data.model.PlaceInfo
import br.com.symon.data.model.requests.SendSaleRequest
import br.com.symon.injection.components.DaggerSendSaleActivityComponent
import br.com.symon.injection.components.SendSaleActivityComponent
import br.com.symon.injection.modules.SendSaleActivityModule
import br.com.symon.ui.SearchPlacesActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_post_info.*
import kotlinx.android.synthetic.main.view_custom_toolbar.*
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import java.text.NumberFormat
import java.util.*


class SendSaleActivity : BaseActivity(), SendSaleContract.View {
    companion object {
        const val EXTRA_URI = "EXTRA_URI"
        const val EXTRA_TOKEN = "EXTRA_TOKEN"
        const val MAX_ADDRESSES = 1
        const val REQUEST_PLACE_INFO = 13123

        private lateinit var uri: Uri
        private lateinit var token: String
        private lateinit var locationProvider: ReactiveLocationProvider

        fun newIntent(context: Context, uri: Uri?, token: String): Intent {
            val intent = Intent(context, SendSaleActivity::class.java)
            intent.putExtra(EXTRA_URI, uri)
            intent.putExtra(EXTRA_TOKEN, token)
            return intent
        }
    }

    private val sendSaleActivityComponent: SendSaleActivityComponent

    get() = DaggerSendSaleActivityComponent
            .builder()
            .applicationComponent((application as CustomApplication).applicationComponent)
            .sendSaleActivityModule(SendSaleActivityModule(this))
            .build()

    private var lat: Double = 0.0
    private var lng: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_info)

        sendSaleActivityComponent.inject(this)

        uri = intent.getParcelableExtra(EXTRA_URI)
        token = intent.getStringExtra(EXTRA_TOKEN)

        setSupportActionBar(mainToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        locationProvider = ReactiveLocationProvider(this)

        customToolbarTitleTextView.text = getString(R.string.post_into_title)
        customToolbarBackImageView.setOnClickListener {
            onBackPressed()
        }

        sendSaleLocationEditText.inputType = InputType.TYPE_NULL

        sendSaleLocationEditText.setOnClickListener {
            val requestIntent = Intent(this, SearchPlacesActivity::class.java)
            startActivityForResult(requestIntent, REQUEST_PLACE_INFO)
        }

        sendSaleLocationEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val requestIntent = Intent(this, SearchPlacesActivity::class.java)
                startActivityForResult(requestIntent, REQUEST_PLACE_INFO)
            }
        }

        sendSaleContinueButton.setOnClickListener {

            if (isValidFields()) {
                val format = NumberFormat.getInstance(Locale("pt", "BR"))
                val number = format.parse(sendSalePriceEditText.text.toString().replace("R$", ""))
                val sendSaleRequest = SendSaleRequest(productName = sendSaleNameEditText.text.toString(),
                        placeName = sendSaleLocationEditText.text.toString(),
                        price = number.toDouble(),
                        lat = lat,
                        lng = lng)

                sendSaleActivityComponent.sendSalePresenter().sendSale(token, sendSaleRequest)
            }
        }

        sendSalePriceEditText.addTextChangedListener(MoneyTextWatcher(sendSalePriceEditText))

        sendSaleViewHeaderProgress.bind(1)

        sendSaleImageView.loadUrl(uri)

        RxPermissions(this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe({ granted ->
                    if (granted) {
                        getLastKnowLocation()
                    } else {
                        toast(getString(R.string.general_permissions_message))
                    }
                })
    }

    override fun onBackPressed() {
        val returnIntent = Intent()
        setResult(Activity.RESULT_CANCELED, returnIntent)
        super.onBackPressed()
    }

    override fun notifySendSuccessfully(id: Int?) {
        id?.let {
            sendSaleActivityComponent.sendSalePresenter().uploadUserPhoto(token, it, uri)
        }
    }

    override fun notifySendPhotoSuccessfully(uri: String?) {
        Log.i("sendPhotoEvent:", uri)
        val returnIntent = Intent()
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        when (resultCode) {
            Activity.RESULT_OK -> {
                if (requestCode == REQUEST_PLACE_INFO) {
                    val placeInfo: PlaceInfo? = data.getParcelableExtra(SearchPlacesActivity.EXTRA_PLACE_INFO)

                    placeInfo?.let {
                        sendSaleLocationEditText.setText("${it.name} - ${it.address}")

                        locationProvider.getPlaceById(it.id).subscribe {
                            val place = it.get(0)
                            lat = place.latLng.latitude
                            lng = place.latLng.longitude
                        }
                    }
                }
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnowLocation() {
        locationProvider.lastKnownLocation
                .subscribe({
                    getAddressFromLocation(it)
                })
    }

    private fun isValidFields(): Boolean {
        if (sendSaleNameEditText.text.isEmpty()) {
            sendSaleNameTextInput.isErrorEnabled = true
            sendSaleNameTextInput.error = getString(R.string.general_required_field)
            return false
        } else {
            sendSaleNameTextInput.isErrorEnabled = false
        }

        if (sendSalePriceEditText.text.toString().replace("R$", "").isEmpty()) {
            sendSalePriceEditText.error =  getString(R.string.general_required_field)
            return false
        }

        return true
    }

    private fun getAddressFromLocation(location: Location) {
        val reverseGeocodeObservable: Observable<MutableList<Address>> =
                locationProvider.getReverseGeocodeObservable(
                        location.latitude, location.longitude, MAX_ADDRESSES)

        reverseGeocodeObservable
                .subscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    lat = it[0].latitude
                    lng = it[0].longitude
                    sendSaleLocationEditText.setText(it[0].getAddressLine(0))
                })
    }
}