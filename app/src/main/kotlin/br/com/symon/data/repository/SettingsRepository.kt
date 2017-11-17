package br.com.symon.data.repository

import br.com.symon.base.BaseRepository
import br.com.symon.data.cache.SettingsCacheManagerImpl
import br.com.symon.data.model.Settings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(private val settingsCacheManagerImpl: SettingsCacheManagerImpl) : BaseRepository() {
    fun saveNotificationsSettings(settings: Settings) = call(settingsCacheManagerImpl.saveNotificationsSettings(settings))
    fun getNotificationsSettings() = call(settingsCacheManagerImpl.getNotificationsSettings())
}