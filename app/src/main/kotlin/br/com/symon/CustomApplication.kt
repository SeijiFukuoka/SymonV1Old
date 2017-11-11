package br.com.symon

import android.app.Application
import br.com.symon.injection.components.ApplicationComponent
import br.com.symon.injection.components.DaggerApplicationComponent
import br.com.symon.injection.modules.ApplicationModule
import br.com.symon.injection.modules.NetworkModule
import br.com.symon.injection.modules.RepositoryModule
import com.facebook.appevents.AppEventsLogger
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class CustomApplication : Application() {

    val applicationComponent: ApplicationComponent
        get() = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .networkModule(NetworkModule())
                .repositoryModule(RepositoryModule())
                .build()

    override fun onCreate() {
        super.onCreate()

        applicationComponent.inject(this)

        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.defaultFont))
                .setFontAttrId(R.attr.fontPath)
                .build())


        AppEventsLogger.activateApp(this)
    }
}