package br.com.symon.data.cache

import android.content.Context
import br.com.symon.data.cache.CacheSettings.SETTINGS_CACHE_NAME
import br.com.symon.data.cache.CacheSettings.SETTINGS_KEY
import br.com.symon.data.model.Settings
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsCacheManagerImpl @Inject constructor(context: Context) : SettingsCacheManager {

    private var cacheManager: CacheManager? = null

    init {
        cacheManager = CacheManagerImpl(context, SETTINGS_CACHE_NAME)
    }

    override fun saveNotificationsSettings(settings: Settings): Observable<Void> = Observable.create {
        deleteSettings()
        cacheManager?.put(SETTINGS_KEY, settings)
    }

    override fun getNotificationsSettings(): Observable<Settings?> = Observable.just(cacheManager?.get(SETTINGS_KEY, Settings::class.java))

    private fun deleteSettings() {
        cacheManager?.delete(SETTINGS_KEY)
    }
}