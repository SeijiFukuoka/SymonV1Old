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
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.common.loadUrl
import br.com.symon.common.toast
import br.com.symon.data.model.PlaceInfo
import com.google.android.gms.location.places.Place
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_post_info.*
import kotlinx.android.synthetic.main.view_custom_toolbar.*
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider


class PostInfoActivity : BaseActivity() {
    companion object {
        const val EXTRA_URI = "EXTRA_URI"
        const val MAX_ADDRESSES = 1
        const val REQUEST_PLACE_INFO = 13123

        private lateinit var uri: Uri
        private lateinit var locationProvider: ReactiveLocationProvider

        fun newIntent(context: Context, uri: Uri?): Intent {
            val intent = Intent(context, PostInfoActivity::class.java)
            intent.putExtra(EXTRA_URI, uri)
            return intent
        }
    }

    private var place: Place? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_info)

        uri = intent.getParcelableExtra(EXTRA_URI)

        setSupportActionBar(mainToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        locationProvider = ReactiveLocationProvider(this)

        customToolbarTitleTextView.text = getString(R.string.post_into_title)
        customToolbarBackImageView.setOnClickListener {
            onBackPressed()
        }

        postInfoLocationEditText.inputType = InputType.TYPE_NULL

        postInfoLocationEditText.setOnClickListener {
            val requestIntent = Intent(this, SearchPlacesActivity::class.java)
            startActivityForResult(requestIntent, REQUEST_PLACE_INFO)
        }

        postInfoLocationEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val requestIntent = Intent(this, SearchPlacesActivity::class.java)
                startActivityForResult(requestIntent, REQUEST_PLACE_INFO)
            }
        }

        postInfoViewHeaderProgress.bind(1)

        postInfoImageView.loadUrl(uri)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        when (resultCode) {
            Activity.RESULT_OK -> {
                if (requestCode == REQUEST_PLACE_INFO) {
                    val placeInfo: PlaceInfo? = data.getParcelableExtra(SearchPlacesActivity.EXTRA_PLACE_INFO)

                    placeInfo?.let {
                        postInfoLocationEditText.setText("${it.name} - ${it.address}")

                        locationProvider.getPlaceById(it.id).subscribe {
                            place = it.get(0)
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

    private fun getAddressFromLocation(location: Location) {
        val reverseGeocodeObservable: Observable<MutableList<Address>> =
                locationProvider.getReverseGeocodeObservable(
                        location.latitude, location.longitude, MAX_ADDRESSES)

        reverseGeocodeObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    postInfoLocationEditText.setText(it[0].getAddressLine(0))
                })
    }

}