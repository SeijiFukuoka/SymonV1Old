package br.com.symon.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.WindowManager
import br.com.symon.R
import br.com.symon.data.model.PlaceInfo
import br.com.symon.ui.send.adapter.SearchPlacesAdapter
import com.google.android.gms.location.places.AutocompletePredictionBuffer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.jakewharton.rxbinding.widget.RxTextView
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_search_places.*
import kotlinx.android.synthetic.main.view_custom_toolbar.*
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import java.util.concurrent.TimeUnit


class SearchPlacesActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PLACE_INFO = "place_info"

        private lateinit var locationProvider: ReactiveLocationProvider
        private lateinit var searchPlacesAdapter: SearchPlacesAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_places)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setSupportActionBar(mainToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        locationProvider = ReactiveLocationProvider(this)

        customToolbarTitleTextView.text = getString(R.string.post_info_location_hint)
        customToolbarBackImageView.setImageResource(R.drawable.ic_close_primary_color)
        customToolbarBackImageView.setOnClickListener {
            onBackPressed()
        }

        searchPlacesViewHeaderProgress.bind(2)

        requestLocationPermission()

        val observable = RxTextView.textChanges(searchPlaceEditText)
                .debounce(300, TimeUnit.MILLISECONDS)
                .map<String> { charSequence -> charSequence.toString() }

        observable.subscribe { query ->
            searchPlace(query)
        }
    }

    override fun onBackPressed() {
        val returnIntent = Intent()
        setResult(Activity.RESULT_CANCELED, returnIntent)
        super.onBackPressed()
    }

    private fun requestLocationPermission() {
        RxPermissions(this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe({ granted ->
                    if (granted) {
                        setupRecyclerView()
                    } else {
                        val alertDialog = AlertDialog.Builder(this).create()
                        alertDialog.setMessage(getString(R.string.general_permissions_message))
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "ok", { _, _ ->
                            onBackPressed()
                        })
                    }
                })
    }

    @SuppressLint("MissingPermission")
    private fun searchPlace(queryString: String) {
        locationProvider.lastKnownLocation
                .subscribe({
                    val latitude = it.latitude
                    val longitude = it.longitude
                    val bounds = LatLngBounds(
                            LatLng(latitude - 0.05, longitude - 0.05),
                            LatLng(latitude + 0.05, longitude + 0.05))


                    val obs: Observable<AutocompletePredictionBuffer> =
                            locationProvider.getPlaceAutocompletePredictions(queryString, bounds, null)

                    obs.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                val placesList = it.map { PlaceInfo(it.placeId, it.getPrimaryText(null).toString(), it.getSecondaryText(null).toString()) }
                                it.release()

                                searchPlacesAdapter.updateAddressList(placesList)
                            })
                })

    }

    private fun setupRecyclerView() {
        searchPlacesAdapter = SearchPlacesAdapter({
            val returnIntent = Intent()
            returnIntent.putExtra(EXTRA_PLACE_INFO, it)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        })

        searchPlaceRecyclerView.layoutManager = LinearLayoutManager(this)
        searchPlaceRecyclerView.adapter = searchPlacesAdapter
        searchPlaceRecyclerView.setHasFixedSize(true)
    }
}