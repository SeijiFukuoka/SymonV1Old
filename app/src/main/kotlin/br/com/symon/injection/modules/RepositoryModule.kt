package br.com.symon.injection.modules

import br.com.symon.data.cache.UserCacheManagerImpl
import br.com.symon.data.repository.UserRepository
import br.com.symon.data.webservice.UserApiService
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    fun provideUserRepository(userApiService: UserApiService, userCacheManagerImpl: UserCacheManagerImpl): UserRepository {
        return UserRepository(userApiService, userCacheManagerImpl)
    }
}