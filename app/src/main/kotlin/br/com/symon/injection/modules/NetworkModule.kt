package br.com.symon.injection.modules

import br.com.symon.data.webservice.*
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
    fun provideBaseApi(): Retrofit = ApiManager.initRetrofit(
            ApiSettings.API_URL)

    @Provides
    @Singleton
    fun providesUserApiService(@Named("provideBaseApi") retrofit: Retrofit): UserApiService =
            retrofit.create<UserApiService>(UserApiService::class.java)

    @Provides
    @Singleton
    fun providesSaleApiService(@Named("provideBaseApi") retrofit: Retrofit): SaleApiService =
            retrofit.create<SaleApiService>(SaleApiService::class.java)

    @Provides
    @Singleton
    fun providesCommentApiService(@Named("provideBaseApi") retrofit: Retrofit): CommentApiService =
            retrofit.create<CommentApiService>(CommentApiService::class.java)

    @Provides
    @Singleton
    fun providesBlockedUsersApiService(@Named("provideBaseApi") retrofit: Retrofit): BlockedUsersApiService =
            retrofit.create<BlockedUsersApiService>(BlockedUsersApiService::class.java)

    @Provides
    @Singleton
    fun providesNotificationApiService(@Named("provideBaseApi") retrofit: Retrofit): NotificationApiService =
            retrofit.create<NotificationApiService>(NotificationApiService::class.java)
}