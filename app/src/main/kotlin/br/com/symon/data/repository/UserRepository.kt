package br.com.symon.data.repository

import br.com.symon.base.BaseRepository
import br.com.symon.data.cache.UserCacheManagerImpl
import br.com.symon.data.model.User
import br.com.symon.data.model.UserTokenRequest
import br.com.symon.data.webservice.UserApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userApiService: UserApiService, private val userCacheManagerImpl: UserCacheManagerImpl) : BaseRepository() {
    fun saveUserCache(user: User) = call(userCacheManagerImpl.save(user))
    fun getUserCache() = call(userCacheManagerImpl.getUser())
    fun deleteCache() = call(userCacheManagerImpl.deleteUser())

    fun getUser(userId: Int) = call(userApiService.getUser(userId))
    fun checkUser(userEmail: String) = call(userApiService.checkUser(userEmail))
    fun getUserToken(userTokenRequest: UserTokenRequest) = call((userApiService.getToken(userTokenRequest)))
    fun registryUser(user: User) = call(userApiService.registryUser(user))
    fun deleteUser(userId: Int) = call(userApiService.deleteUser(userId))
    fun updateUser(user: User) = call(userApiService.updateUser(user))
    fun uploadUserPhoto(photo: MultipartBody.Part, name: RequestBody) =
            call(userApiService.uploadUserPhoto(photo, name))
}