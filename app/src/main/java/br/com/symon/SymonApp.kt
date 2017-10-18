package br.com.symon

import android.app.Application
import br.com.symon.di.AppModule
import br.com.symon.di.news.DaggerNewsComponent
import br.com.symon.di.news.NewsComponent
import br.com.symon.tracking.Tracking
import com.crashlytics.android.core.CrashlyticsCore
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

class SymonApp : Application() {

    companion object {
        lateinit var newsComponent: NewsComponent
    }

    override fun onCreate() {
        super.onCreate()
        val core = CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build()
        Fabric.with(this, Crashlytics.Builder().core(core).build(), Crashlytics())

        Tracking.initialize(this)
        newsComponent = DaggerNewsComponent.builder()
                .appModule(AppModule(this))
                //.newsModule(NewsModule()) Module with empty constructor is implicitly created by dagger.
                .build()
    }
}