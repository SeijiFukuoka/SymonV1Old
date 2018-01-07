package br.com.symon

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import br.com.symon.injection.components.ApplicationComponent
import br.com.symon.injection.components.DaggerApplicationComponent
import br.com.symon.injection.modules.ApplicationModule
import br.com.symon.injection.modules.NetworkModule
import com.crashlytics.android.Crashlytics
import com.facebook.appevents.AppEventsLogger
import io.fabric.sdk.android.Fabric
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


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

        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.defaultFont))
                .setFontAttrId(R.attr.fontPath)
                .build())

        if (!BuildConfig.DEBUG)
            Fabric.with(this, Crashlytics())

        AppEventsLogger.activateApp(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}