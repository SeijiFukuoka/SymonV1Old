package br.com.symon.data.webservice

import br.com.symon.data.model.User
import br.com.symon.data.model.requests.*
import br.com.symon.data.model.responses.CheckUserResponse
import br.com.symon.data.model.responses.RegisterUserResponse
import br.com.symon.data.model.responses.UploadPhotoResponse
import br.com.symon.data.model.responses.UserTokenResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserApiService {

    @GET("/user/{user_id}")
    fun getUser(@Path("user_id") userId: Int): Observable<User>

    @GET("/user/check/{user_email}")
    fun checkUser(@Path("user_email") userEmail: String?): Observable<CheckUserResponse>

    @POST("/user")
    fun registryUser(@Body userAuthenticateRequest: UserAuthenticateRequest)
            : Observable<Response<RegisterUserResponse>>

    @POST("/user")
    fun registryUserFacebook(@Body user: UserFacebookRegistryRequest)
            : Observable<RegisterUserResponse>

    @DELETE("/user/{user_id}")
    fun deleteUser(@Path("user_id") userId: Int): Observable<Response<Void>>

    @PUT("/user/{user_id}")
    fun updateUser(@Path("user_id") userId: Int,
                   @Body userUpdateRequest: UserUpdateRequest): Observable<Response<UserTokenResponse>>

    @PUT("/user/{user_id}")
    fun updateFullUser(@Path("user_id") userId: Int,
                   @Body userFullUpdateRequest: UserFullUpdateRequest): Observable<Response<UserTokenResponse>>

    @Multipart
    @POST("/user/photo/{user_id}")
    fun uploadUserPhoto(@Path("user_id") userId: Int,
                        @Part photo: MultipartBody.Part): Observable<UploadPhotoResponse>

    @POST("/token")
    fun getToken(@Body userAuthenticateRequest: UserAuthenticateRequest): Observable<Response<UserTokenResponse>>

    @POST("/user/retrievePassword/{user_email}")
    fun retrievePassword(@Path("user_email") userEmail: String?): Observable<Response<Void>>

    @POST("/user/block")
    fun blockUser(@Header("Authorization") userToken: String?, @Body userBlockedId: BlockUserRequest?): Observable<Response<Void>>
}