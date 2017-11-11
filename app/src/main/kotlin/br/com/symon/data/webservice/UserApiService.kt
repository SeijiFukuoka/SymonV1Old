package br.com.symon.data.webservice

import br.com.symon.data.model.User
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface UserApiService {

    @GET("/user/{user_id}")
    fun getUser(@Path("user_id") userId: Int): Observable<User>

    @POST("/user")
    @FormUrlEncoded
    fun registryUser(@Body user: User): Observable<User>

    @DELETE("/user/{user_id}")
    fun deleteUser(@Path("user_id") userId: Int): Observable<Void>

    @PUT("/user/{user_id}")
    @FormUrlEncoded
    fun updateUser(@Body user: User): Observable<Void>

    @Multipart
    @POST("/user/photo")
    fun uploadUserPhoto(@Part photo: MultipartBody.Part,
                        @Part("resource") name: RequestBody): Observable<ResponseBody>
}