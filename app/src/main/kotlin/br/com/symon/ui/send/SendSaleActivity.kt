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
import android.view.View
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.common.loadUrl
import br.com.symon.common.parseToBigDecimal
import br.com.symon.common.toast
import br.com.symon.common.widget.MoneyTextWatcher
import br.com.symon.data.model.Constants.Companion.NEED_UPDATE_RESULT
import br.com.symon.data.model.PlaceInfo
import br.com.symon.data.model.Sale
import br.com.symon.data.model.requests.SendSaleRequest
import br.com.symon.injection.components.DaggerSendSaleActivityComponent
import br.com.symon.injection.components.SendSaleActivityComponent
import br.com.symon.injection.modules.SendSaleActivityModule
import br.com.symon.ui.SearchPlacesActivity
import br.com.symon.ui.saleDetail.SaleDetailActivity.Companion.EXTRA_UPDATED_SALE
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_post_info.*
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import java.text.NumberFormat
import java.util.*


class SendSaleActivity : BaseActivity(), SendSaleContract.View {
    companion object {
        const val EXTRA_URI = "EXTRA_URI"
        const val EXTRA_TOKEN = "EXTRA_TOKEN"
        const val EXTRA_SALE = "EXTRA_SALE"
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

        fun newIntent(context: Context, uri: Uri?, token: String, sale: Sale): Intent {
            val intent = Intent(context, SendSaleActivity::class.java)
            intent.putExtra(EXTRA_URI, uri)
            intent.putExtra(EXTRA_TOKEN, token)
            intent.putExtra(EXTRA_SALE, sale)
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
    private var isUpdate: Boolean = false
    private lateinit var sale: Sale
    private lateinit var sendSaleRequest: SendSaleRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_info)

        sendSaleActivityComponent.inject(this)

        uri = intent.getParcelableExtra(EXTRA_URI)
        token = intent.getStringExtra(EXTRA_TOKEN)

        if (intent.getParcelableExtra<Sale>(EXTRA_SALE) != null) {
            sale = intent.getParcelableExtra(EXTRA_SALE)
            isUpdate = true
        }

        setSupportActionBar(mainToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        locationProvider = ReactiveLocationProvider(this)

        sendSaleActivityToolbarTitleTextView.text = getString(R.string.post_into_title)
        sendSaleActivityToolbarBackImageView.setOnClickListener {
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
            continueButtonPressed()
        }

        sendSalePriceEditText.addTextChangedListener(MoneyTextWatcher(sendSalePriceEditText))

        sendSaleViewHeaderProgress.bind(1)

        sendSaleImageView.loadUrl(uri)

        if (isUpdate) {
            sendSaleActivityToolbarSaveTextView.visibility = View.VISIBLE
            sendSaleActivityToolbarSaveTextView.setOnClickListener {
                continueButtonPressed()
            }

            sendSaleNameEditText.setText(sale.message)
            sendSaleLocationEditText.setText(sale.place)
            lat = sale.latitude
            lng = sale.longitude

            sendSalePriceEditText.setText(NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(sale.price))
            sendSaleContinueButton.text = getString(R.string.send_sale_save)
        } else {
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

    override fun updateSaleSuccessfully() {
        val returnIntent = Intent()
        returnIntent.putExtra(EXTRA_UPDATED_SALE, sendSaleRequest)
        setResult(NEED_UPDATE_RESULT, returnIntent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        when (resultCode) {
            Activity.RESULT_OK -> {
                if (requestCode == REQUEST_PLACE_INFO) {
                    val placeInfo: PlaceInfo? = data.getParcelableExtra(SearchPlacesActivity.EXTRA_PLACE_INFO)

                    placeInfo?.let {
                        sendSaleLocationEditText.setText(String.format(getString(R.string.send_sale_formatted_place, it.name, it.address)))

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

    private fun continueButtonPressed() {
        if (isValidFields()) {
            val value = sendSalePriceEditText.text.toString().parseToBigDecimal()
            sendSaleRequest = SendSaleRequest(productName = sendSaleNameEditText.text.toString(),
                    placeName = sendSaleLocationEditText.text.toString(),
                    price = value,
                    lat = lat,
                    lng = lng)

            if (!isUpdate) {
                sendSaleActivityComponent.sendSalePresenter().sendSale(token, sendSaleRequest)
            } else {
                sendSaleActivityComponent.sendSalePresenter().updateSale(sale.id, token, sendSaleRequest)
            }
        }
    }
}