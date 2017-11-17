package br.com.symon.data.cache

import android.content.Context
import br.com.symon.data.cache.CacheSettings.SETTINGS_CACHE_NAME
import br.com.symon.data.cache.CacheSettings.SETTINGS_KEY
import br.com.symon.data.model.Settings
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsCacheManagerImpl @Inject constructor(context: Context) : SettingsCacheManager {

    private var cacheManager: CacheManager? = null

    init {
        cacheManager = CacheManagerImpl(context, SETTINGS_CACHE_NAME)
    }

    override fun saveNotificationsSettings(settings: Settings): Observable<Unit> = Observable.create<Unit> { emitter ->
        deleteSettings()
        emitter.onNext(cacheManager?.put(CacheSettings.SETTINGS_KEY, settings)!!)
        emitter.onComplete()
    }

    override fun getNotificationsSettings(): Observable<Settings?> = Observable.create<Settings> { emitter ->
        var settingsCache: Settings? = cacheManager?.get(CacheSettings.SETTINGS_KEY, Settings::class.java)

        if (settingsCache == null)
            settingsCache = Settings()

        emitter.onNext(settingsCache)
        emitter.onComplete()
    }

    private fun deleteSettings() {
        cacheManager?.delete(SETTINGS_KEY)
    }
}