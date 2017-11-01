package br.com.symon.injection.modules

import br.com.symon.data.webservice.ApiManager
import br.com.symon.data.webservice.ApiSettings
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Named("provideBaseApi")
    @Singleton
    fun provideBaseApi(): Retrofit = ApiManager.initRetrofit(ApiSettings.API_URL)
}