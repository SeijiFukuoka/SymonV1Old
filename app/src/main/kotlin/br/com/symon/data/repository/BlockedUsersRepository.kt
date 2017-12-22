package br.com.symon.data.repository

import br.com.symon.base.BaseRepository
import br.com.symon.data.model.requests.BlockUserRequest
import br.com.symon.data.webservice.BlockedUsersApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlockedUsersRepository @Inject constructor(private val blockedUsersApiService: BlockedUsersApiService) : BaseRepository() {

    fun getBlockedUsersList(userToken: String) = call(blockedUsersApiService.getBlockedUsersList(userToken))
    fun blockUser(userToken: String, blockedUserRequest: BlockUserRequest) = call(blockedUsersApiService.blockUser(userToken, blockedUserRequest))
    fun unblockUser(userToken: String, unblockUserRequest: BlockUserRequest) = call(blockedUsersApiService.unblockUser(userToken, unblockUserRequest))

}