package br.com.symon.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import br.com.symon.R
import br.com.symon.base.BaseActivity
import br.com.symon.common.isDisplayedByTag
import br.com.symon.common.replace
import br.com.symon.common.startIntent
import br.com.symon.common.toast
import br.com.symon.data.model.responses.UserTokenResponse
import br.com.symon.ui.profile.ProfileFragment
import br.com.symon.ui.settings.SettingsActivity
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    companion object {
        const val EXTRA_USER = "EXTRA_USER"

        lateinit var userTokenResponse: UserTokenResponse

        fun newIntent(context: Context, user: UserTokenResponse?): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(EXTRA_USER, user)
            return intent
        }
    }

    private lateinit var menuSettings : MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userTokenResponse = intent.getParcelableExtra(EXTRA_USER)

        setSupportActionBar(mainToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupBottomMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        menuSettings = menu.findItem(R.id.menu_action_settings)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_action_settings -> {
                startIntent(SettingsActivity::class.java)
                true
            }
            else -> super.onOptionsItemSelected(item)
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

        mainBottomNavigation.setOnTabSelectedListener { position, _ ->
            when (position) {
                0 -> toast("$position")
                1 -> toast("$position")
                2 -> toast("$position")
                3 -> toast("$position")
                4 -> openProfile()
            }
            true
        }
    }

    private fun openProfile() {
        val profileFragment = ProfileFragment.newInstance(user = userTokenResponse.user)
        if (!isDisplayedByTag(this, ProfileFragment::class.java.canonicalName)) {
            replace(this, R.id.mainFrameContent, profileFragment)
        }

        menuSettings.isVisible = true
    }
}