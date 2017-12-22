package br.com.symon.data.webservice

import br.com.symon.data.model.BlockedUser
import br.com.symon.data.model.requests.BlockUserRequest
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface BlockedUsersApiService {

    @GET("/block/user")
    fun getBlockedUsersList(@Header("Authorization") userToken: String): Observable<Response<MutableList<BlockedUser>>>

    @POST("/block/user")
    fun blockUser(@Header("Authorization") userToken: String, @Body blockedUserRequest: BlockUserRequest): Observable<Response<Void>>


    @HTTP(method = "DELETE", path = "/block/user", hasBody = true)
    @Headers("Content-Type: application/json;charset=UTF-8")
    fun unblockUser(@Header("Authorization") userToken: String, @Body blockedUserRequest: BlockUserRequest): Observable<Response<Void>>
}