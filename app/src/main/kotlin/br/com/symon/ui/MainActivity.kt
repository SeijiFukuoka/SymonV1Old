package br.com.symon.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import br.com.symon.CustomApplication
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.common.isDisplayedByTag
import br.com.symon.common.replace
import br.com.symon.common.toast
import br.com.symon.data.model.User
import br.com.symon.data.model.responses.UserTokenResponse
import br.com.symon.injection.components.DaggerMainActivityComponent
import br.com.symon.injection.components.MainActivityComponent
import br.com.symon.injection.modules.MainActivityModule
import br.com.symon.ui.main.MainContract
import br.com.symon.ui.sales.SalesFragment
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), MainContract.View, SearchView.OnQueryTextListener {
    companion object {
        const val EXTRA_USER = "EXTRA_USER"

        lateinit var user: UserTokenResponse

        fun newIntent(context: Context, user: UserTokenResponse?): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(EXTRA_USER, user)
            return intent
        }
    }

    private val mainActivityComponent: MainActivityComponent
        get() = DaggerMainActivityComponent
                .builder()
                .applicationComponent((this.application as CustomApplication).applicationComponent)
                .mainActivityModule(MainActivityModule(this))
                .build()

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainActivityComponent.inject(this)
        mainActivityComponent.mainPresenter().getUserCache()

        setupSearchView()
        setupBottomMenu()
        openSales()
    }

    override fun onBackPressed() = if (!mainActivitySearchView.isIconified) {
        mainActivitySearchView.setIconified(true)
    } else {
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        toast(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean = true

    override fun setUser(user: User?) {
        this.user = user
    }

    private fun setupSearchView() {
        mainActivitySearchView.setOnQueryTextListener(this)
        mainActivitySearchView.setOnSearchClickListener({
            toast("CLICK")
            mainBrandImageView.visibility = View.GONE
            mainFrameContent.visibility = View.GONE
            mainActivityBackArrowImageView.visibility = View.VISIBLE
        })

        mainActivitySearchView.setOnCloseListener {
            toast("CLOSE")
            mainFrameContent.visibility = View.VISIBLE
            mainBrandImageView.visibility = View.VISIBLE
            mainActivityBackArrowImageView.visibility = View.GONE
            false
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
                2 -> openSendSale()
                3 -> openNotifications()
                4 -> openProfile()
            }
            true
        }
    }

    private fun openSales() {
        if (!isDisplayedByTag(this, SalesFragment::class.java.canonicalName)) {
            replace(this, R.id.mainFrameContent, SalesFragment())
        }
    }

    private fun openRatings() {
        toast("Avaliações - Em progresso")
    }

    private fun openSendSale() {
        toast("Enviar - Em progresso")
    }

    private fun openNotifications() {
        toast("Notificações - Em progresso")
    }

    private fun openProfile() {
        toast("Perfil - Em progresso")
    }
}