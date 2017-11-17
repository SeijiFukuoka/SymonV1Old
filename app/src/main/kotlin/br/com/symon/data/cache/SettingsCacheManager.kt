package br.com.symon.data.cache

import br.com.symon.data.model.Settings
import io.reactivex.Observable


interface SettingsCacheManager {
    fun saveNotificationsSettings(settings: Settings): Observable<Unit>
    fun getNotificationsSettings(): Observable<Settings?>
}