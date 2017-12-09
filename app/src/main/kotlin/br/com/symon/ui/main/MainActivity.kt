package br.com.symon.ui.main

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.common.isDisplayedByTag
import br.com.symon.common.replace
import br.com.symon.common.startIntent
import br.com.symon.common.toast
import br.com.symon.data.model.responses.SalesListResponse
import br.com.symon.data.model.responses.UserTokenResponse
import br.com.symon.injection.components.DaggerMainActivityComponent
import br.com.symon.injection.components.MainActivityComponent
import br.com.symon.injection.modules.MainActivityModule
import br.com.symon.ui.profile.ProfileFragment
import br.com.symon.ui.sales.SalesFragment
import br.com.symon.ui.sendPost.PostInfoActivity
import br.com.symon.ui.settings.SettingsActivity
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_choose_photo_dialog.view.*

class MainActivity : BaseActivity(), MainContract.View, SearchView.OnQueryTextListener {
    companion object {
        const val EXTRA_USER = "EXTRA_USER"
        const val REQUEST_PICK_IMAGE_FROM_CAMERA: Int = 12130
        const val REQUEST_PICK_IMAGE_FROM_GALLERY = 10017

        lateinit var userTokenResponse: UserTokenResponse

        fun newIntent(context: Context, user: UserTokenResponse?): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(EXTRA_USER, user)
            return intent
        }
    }

    private val mainActivityComponent: MainActivityComponent
        get() = DaggerMainActivityComponent
                .builder()
                .applicationComponent((application as CustomApplication).applicationComponent)
                .mainActivityModule(MainActivityModule(this))
                .build()

    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainActivityComponent.inject(this)

        userTokenResponse = intent.getParcelableExtra(EXTRA_USER)

        setSupportActionBar(mainToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupBottomMenu()
        setupSearchView()

        mainSettingsImageView.setOnClickListener {
            startIntent(SettingsActivity::class.java)
        }
    }

    override fun onBackPressed() = if (!mainActivitySearchView.isIconified) {
        mainActivitySearchView.setIconified(true)
    } else {
        super.onBackPressed()
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        openSearchSales(query)
        mainActivitySearchView.setQuery("", false)
        mainActivitySearchView.isIconified = true
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean = false

    override fun showSearchResults(salesListResponse: SalesListResponse) {
        if (salesListResponse.salesList.size > 0)
            toast("Resultados da procura = ${salesListResponse.salesList.get(0)} ")
        else
            toast("Nenhum resultado encontrado")
    }

    private fun setupSearchView() {
        mainActivitySearchView.setOnQueryTextListener(this)
        mainActivitySearchView.setOnSearchClickListener({
            mainBrandImageView.visibility = View.GONE
            mainFrameContent.visibility = View.GONE
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        })

        mainActivitySearchView.setOnCloseListener {
            mainFrameContent.visibility = View.VISIBLE
            mainBrandImageView.visibility = View.VISIBLE
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_PICK_IMAGE_FROM_GALLERY -> {
                    val postInfoIntent = PostInfoActivity.newIntent(this, data?.data)
                    startActivity(postInfoIntent)
                    mainBottomNavigation.currentItem = 0
                }
                REQUEST_PICK_IMAGE_FROM_CAMERA -> {
                    val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                    mediaScanIntent.data = photoUri
                    sendBroadcast(mediaScanIntent)

                    val postInfoIntent = PostInfoActivity.newIntent(this, photoUri)
                    startActivity(postInfoIntent)

                    mainBottomNavigation.currentItem = 0
                }
            }
        }
    }

    private fun setupBottomMenu() {
        val itemPromos = AHBottomNavigationItem(R.string.tab_promo, R.drawable.ic_promos, R.color.black)
        val itemRating = AHBottomNavigationItem(R.string.tab_rating, R.drawable.ic_rating,
                R.color.black)
        val itemSend = AHBottomNavigationItem(R.string.tab_send, R.drawable.ic_send, R.color.black)
        val itemNotifications = AHBottomNavigationItem(R.string.tab_notifications,
                R.drawable.ic_notification, R.color.black)
        val itemProfile = AHBottomNavigationItem(R.string.tab_profile, R.drawable.ic_profile,
                R.color.black)

        mainBottomNavigation.addItem(itemPromos)
        mainBottomNavigation.addItem(itemRating)
        mainBottomNavigation.addItem(itemSend)
        mainBottomNavigation.addItem(itemNotifications)
        mainBottomNavigation.addItem(itemProfile)

        mainBottomNavigation.accentColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        mainBottomNavigation.isTranslucentNavigationEnabled = true
        mainBottomNavigation.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW

        mainBottomNavigation.setNotificationBackgroundColor(
                ContextCompat.getColor(this, R.color.colorAccent))
        mainBottomNavigation.setNotification("!", 3)

        mainBottomNavigation.isBehaviorTranslationEnabled = true

        mainBottomNavigation.setOnTabSelectedListener { position, _ ->
            when (position) {
                0 -> openSales()
                1 -> openRatings()
                2 -> openSendSaleDialog()
                3 -> openNotifications()
                4 -> openProfile()
            }
            true
        }

        mainBottomNavigation.currentItem = 0
    }

    private fun openProfile() {
        val profileFragment = ProfileFragment.newInstance(user = userTokenResponse.user)
        if (!isDisplayedByTag(ProfileFragment::class.java.canonicalName)) {
            replace(R.id.mainFrameContent, profileFragment)
        }
        setupToolbarMenu(isSearchVisible = false, isSettingsVisible = true)
    }

    private fun openSales() {
        if (!isDisplayedByTag(SalesFragment::class.java.canonicalName)) {
            replace(R.id.mainFrameContent, SalesFragment())
        }
        setupToolbarMenu(isSearchVisible = true, isSettingsVisible = false)
    }

    private fun openSearchSales(searchQuery: String) {
        replace(R.id.mainFrameContent, SalesFragment.newInstance(searchQuery))
        setupToolbarMenu(isSearchVisible = true, isSettingsVisible = false)
    }

    private fun openRatings() {
        toast("Avaliações - Em progresso")
        setupToolbarMenu(isSearchVisible = false, isSettingsVisible = false)
    }

    private fun openSendSaleDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.view_choose_photo_dialog, null)

        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        val alertDialog = dialogBuilder.show()

        dialogView.choosePhotoCamContainerLinearLayout.setOnClickListener {
            pickImageFromCam()
            alertDialog.hide()
        }

        dialogView.choosePhotoGalleryContainerLinearLayout.setOnClickListener {
            pickImageFromGalley()
            alertDialog.hide()
        }

        dialogView.choosePhotoCancelTextView.setOnClickListener {
            alertDialog.hide()
            mainBottomNavigation.currentItem = 0
        }
    }

    override fun callCameraFromUri(uri: Uri?) {
        photoUri = uri
        RxPermissions(this)
                .request(Manifest.permission.CAMERA)
                .subscribe { granted ->
                    if (granted) {
                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                        startActivityForResult(cameraIntent, REQUEST_PICK_IMAGE_FROM_CAMERA)
                    } else {
                        toast(getString(R.string.general_permissions_message))
                    }
                }
    }

    private fun pickImageFromCam() {
        RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    if (granted) {
                        mainActivityComponent.mainPresenter().setPathUri()
                    } else {
                        toast(getString(R.string.general_permissions_message))
                    }
                }
    }

    private fun pickImageFromGalley() {
        RxPermissions(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    if (granted) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                            startActivityForResult(Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), REQUEST_PICK_IMAGE_FROM_GALLERY)
                        } else {
                            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                            intent.addCategory(Intent.CATEGORY_OPENABLE)
                            intent.type = "image/*"
                            startActivityForResult(intent, REQUEST_PICK_IMAGE_FROM_GALLERY)
                        }
                    } else {
                        toast(getString(R.string.general_permissions_message))
                    }
                }
    }

    private fun openNotifications() {
        toast("Notificações - Em progresso")
        setupToolbarMenu(isSearchVisible = false, isSettingsVisible = false)
    }

    private fun setupToolbarMenu(isSearchVisible: Boolean, isSettingsVisible: Boolean) {

        mainActivitySearchView.visibility =
                if (isSearchVisible)
                    View.VISIBLE
                else
                    View.GONE

        mainSettingsImageView.visibility =
                if (isSettingsVisible)
                    View.VISIBLE
                else
                    View.GONE
    }

}