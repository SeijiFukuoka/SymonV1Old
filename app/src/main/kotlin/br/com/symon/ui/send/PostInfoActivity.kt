package br.com.symon.ui.send

import android.Manifest
import android.annotation.SuppressLint
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
import br.com.symon.common.startIntent
import br.com.symon.common.toast
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

        lateinit var uri: Uri
        lateinit var locationProvider: ReactiveLocationProvider

        fun newIntent(context: Context, uri: Uri?): Intent {
            val intent = Intent(context, PostInfoActivity::class.java)
            intent.putExtra(EXTRA_URI, uri)
            return intent
        }
    }

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
            startIntent(SearchPlacesActivity::class.java)
        }

        postInfoLocationEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                startIntent(SearchPlacesActivity::class.java)
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