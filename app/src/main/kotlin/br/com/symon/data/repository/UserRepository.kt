package br.com.symon.data.repository

import br.com.symon.base.BaseRepository
import br.com.symon.data.cache.UserCacheManagerImpl
import br.com.symon.data.model.requests.*
import br.com.symon.data.model.responses.UserTokenResponse
import br.com.symon.data.webservice.UserApiService
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userApiService: UserApiService,
                                         private val userCacheManagerImpl: UserCacheManagerImpl)
    : BaseRepository() {

    fun saveUserCache(user: UserTokenResponse?) = call(userCacheManagerImpl.save(user))

    fun getUserCache() = call(userCacheManagerImpl.getUser())

    fun deleteCache() = call(userCacheManagerImpl.deleteUser())

    fun getUser(userId: Int) = call(userApiService.getUser(userId))

    fun checkUser(userEmail: String) = call(userApiService.checkUser(userEmail))

    fun getUserToken(userAuthenticateRequest: UserAuthenticateRequest)
            = call((userApiService.getToken(userAuthenticateRequest)))

    fun registryUser(userAuthenticateRequest: UserAuthenticateRequest)
            = call(userApiService.registryUser(userAuthenticateRequest))

    fun registryUserFacebook(user: UserFacebookRegistryRequest)
            = call(userApiService.registryUserFacebook(user))

    fun deleteUser(userId: Int) = call(userApiService.deleteUser(userId))

    fun updateUser(userId: Int, userUpdateRequest: UserUpdateRequest) =
            call(userApiService.updateUser(userId, userUpdateRequest))

    fun updateFullUser(userId: Int, userFullUpdateRequest: UserFullUpdateRequest) =
            call(userApiService.updateFullUser(userId, userFullUpdateRequest))

    fun uploadUserPhoto(userId: Int, photo: MultipartBody.Part)
            = call(userApiService.uploadUserPhoto(userId, photo))

    fun retrievePassword(userEmail: String) = call(userApiService.retrievePassword(userEmail))

    fun blockUSer(userToken: String?, userBlockedId: BlockUserRequest?) = call(userApiService.blockUser(userToken, userBlockedId))

    fun getFavorites(userToken: String, page: Int, pageSize: Int) = call(userApiService.getFavorites(userToken, page, pageSize))

    fun getLikes(userToken: String, page: Int, pageSize: Int) = call(userApiService.getLikes(userToken, page, pageSize))

    fun getDislikes(userToken: String, page: Int, pageSize: Int) = call(userApiService.getDislikes(userToken, page, pageSize))

    fun getComments(userToken: String, page: Int, pageSize: Int) = call(userApiService.getComments(userToken, page, pageSize))
}