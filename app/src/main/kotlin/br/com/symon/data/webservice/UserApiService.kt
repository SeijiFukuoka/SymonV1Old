package br.com.symon.data.webservice

import br.com.symon.data.model.User
import br.com.symon.data.model.requests.*
import br.com.symon.data.model.responses.*
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

    @POST("/forgot")
    fun retrievePassword(@Header("Authorization") userToken: String): Observable<Response<RetrievePasswordResponse>>

    @GET("/user/favorite")
    fun getFavorites(@Header("Authorization") userToken: String,
                     @Query("page") page: Int,
                     @Query("pageSize") pageSize: Int,
                     @Query("order") order: Int): Observable<SalesListResponse>

    @GET("/user/like")
    fun getLikes(@Header("Authorization") userToken: String,
                 @Query("page") page: Int,
                 @Query("pageSize") pageSize: Int,
                 @Query("order") order: Int): Observable<SalesListResponse>

    @GET("/user/dislike")
    fun getDislikes(@Header("Authorization") userToken: String,
                    @Query("page") page: Int,
                    @Query("pageSize") pageSize: Int,
                    @Query("order") order: Int): Observable<SalesListResponse>

    @GET("/user/comment")
    fun getComments(@Header("Authorization") userToken: String,
                    @Query("page") page: Int,
                    @Query("pageSize") pageSize: Int,
                    @Query("order") order: Int): Observable<SalesListResponse>

    @GET("/user/sale")
    fun getSales(@Header("Authorization") userToken: String,
                 @Query("page") page: Int,
                 @Query("pageSize") pageSize: Int): Observable<SalesListResponse>

}