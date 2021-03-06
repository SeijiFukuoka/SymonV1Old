package br.com.symon.data.webservice

import br.com.symon.data.model.Comment
import br.com.symon.data.model.Sale
import br.com.symon.data.model.requests.SaleReportRequest
import br.com.symon.data.model.requests.SendSaleCommentRequest
import br.com.symon.data.model.requests.SendSaleRequest
import br.com.symon.data.model.responses.SalesListResponse
import br.com.symon.data.model.responses.SendSaleResponse
import br.com.symon.data.model.responses.UploadPhotoResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface SaleApiService {

    @GET("/sale")
    fun getSalesList(
            @Header("Authorization") userToken: String,
            @Query("page") page: Int,
            @Query("pageSize") pageSize: Int,
            @Query("radius") radius: Int,
            @Query("latitude") latitude: Double,
            @Query("longitude") longitude: Double
    ): Observable<SalesListResponse>

    @GET("/sale/{sale_id}")
    fun getSaleDetail(@Path("sale_id") saleId: Int): Observable<Response<Sale>>

    @POST("/sale")
    fun uploadSale(@Body sendSaleRequest: SendSaleRequest,
                   @Header("Authorization") userToken: String): Observable<Response<SendSaleResponse>>

    @Multipart
    @POST("/sale/photo/{sale_id}")
    fun uploadSalePhoto(@Path("sale_id") saleId: Int,
                        @Part photo: MultipartBody.Part,
                        @Header("Authorization") userToken: String): Observable<Response<UploadPhotoResponse>>

    @PUT("/sale/{sale_id}")
    fun updateSale(@Path("sale_id") saleId: Int,
                   @Body sendSaleRequest: SendSaleRequest,
                   @Header("Authorization") userToken: String): Observable<Response<Void>>

    @DELETE("/sale/{sale_id}")
    fun deleteSale(@Path("sale_id") saleId: Int): Observable<Response<Void>>

    @POST("/sale/{sale_id}/like")
    fun likeSale(@Path("sale_id") saleId: Int, @Header("Authorization") userToken: String): Observable<Response<Void>>

    @POST("/sale/{sale_id}/dislike")
    fun disLikeSale(@Path("sale_id") saleId: Int, @Header("Authorization") userToken: String): Observable<Response<Void>>

    @GET("/sale/search")
    fun searchSale(@Header("Authorization") userToken: String, @Query("query") query: String, @Query("page") page: Int, @Query("pageSize") pageSize: Int): Observable<SalesListResponse>

    @POST("/sale/report")
    fun reportSale(@Header("Authorization") userToken: String?, @Body saleReportRequest: SaleReportRequest?): Observable<Response<Void>>

    @GET("/sale/{sale_id}/comment")
    fun getComments(@Header("Authorization") userToken: String?, @Path("sale_id") saleId: Int): Observable<MutableList<Comment>>

    @POST("/sale/{sale_id}/comment")
    fun sendComment(@Header("Authorization") userToken: String?, @Path("sale_id") saleId: Int, @Body sendSaleCommentRequest: SendSaleCommentRequest): Observable<Comment>

    @POST("/sale/{sale_id}/favorite")
    fun setFavorite(@Header("Authorization") userToken: String?, @Path("sale_id") saleId: Int): Observable<Response<Void>>
}