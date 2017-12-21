package br.com.symon.data.webservice

import br.com.symon.data.model.User
import br.com.symon.data.model.requests.BlockUserRequest
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface BlockedUsersApiService {

    @GET("/block/user")
    fun getBlockedUsersList(@Header("Authorization") userToken: String): Observable<Response<MutableList<User>>>

    @POST("/block/user")
    fun blockUser(@Header("Authorization") userToken: String, @Body blockedUserRequest: BlockUserRequest): Observable<Response<Void>>

    @DELETE("/block/user")
    fun unblockUser(@Header("Authorization") userToken: String, @Body blockedUserRequest: BlockUserRequest): Observable<Response<Void>>
}