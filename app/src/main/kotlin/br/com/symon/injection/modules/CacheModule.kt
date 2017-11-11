package br.com.symon.injection.modules

import android.content.Context
import br.com.symon.data.cache.SettingsCacheManagerImpl
import br.com.symon.data.cache.UserCacheManagerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton


@Module
class CacheModule {
    @Provides
    @Named("provideSettingsCache")
    @Singleton
    fun providesSettingsCacheManager(context: Context) : SettingsCacheManagerImpl = SettingsCacheManagerImpl(context)

    @Provides
    @Named("provideUserCache")
    @Singleton
    fun providesUserCacheManager(context: Context) : UserCacheManagerImpl = UserCacheManagerImpl(context)
}