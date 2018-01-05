package br.com.symon

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import br.com.symon.injection.components.ApplicationComponent
import br.com.symon.injection.components.DaggerApplicationComponent
import br.com.symon.injection.modules.ApplicationModule
import br.com.symon.injection.modules.NetworkModule
import com.facebook.appevents.AppEventsLogger
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import android.provider.SyncStateContract.Helpers.update
import android.content.pm.PackageManager
import android.content.pm.PackageInfo
import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class CustomApplication : Application() {

    val applicationComponent: ApplicationComponent
        get() = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .networkModule(NetworkModule())
                .build()

    override fun onCreate() {
        super.onCreate()

        applicationComponent.inject(this)

        // Add code to print out the key hash
        try {
            val info = packageManager.getPackageInfo(
                    "br.com.symon",
                    PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }


        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.defaultFont))
                .setFontAttrId(R.attr.fontPath)
                .build())


        AppEventsLogger.activateApp(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}