package br.com.symon.data.webservice

import br.com.symon.data.model.Sale
import br.com.symon.data.model.responses.SalesListResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface SaleApiService {

    @GET("/sale")
    fun getSalesList(@Header("Authorization") userToken: String, @Query("page") page: Int, @Query("pageSize") pageSize: Int): Observable<SalesListResponse>

    @GET("/sale/{sale_id}")
    fun getSaleDetail(@Path("sale_id") saleId: Int): Observable<Response<Sale>>

    @FormUrlEncoded
    @POST("/sale")
    fun uploadSale(@Body sale: Sale): Observable<Response<Sale>>

    @Multipart
    @POST("/sale/photo")
    fun uploaSalePhoto(@Path("sale_id") saleId: Int,
                       @Part photo: MultipartBody.Part): Observable<Response<ResponseBody>>

    @FormUrlEncoded
    @PUT("/sale/{sale_id}")
    fun updateSale(@Body sale: Sale): Observable<Response<Void>>

    @DELETE("/sale/{sale_id}")
    fun deleteSale(@Path("sale_id") saleId: Int): Observable<Response<Void>>

    @POST("/sale/{sale_id}/like")
    fun likeSale(@Path("sale_id") saleId: Int, @Header("Authorization") userToken: String): Observable<Response<Void>>

    @POST("/sale/{sale_id}/dislike")
    fun disLikeSale(@Path("sale_id") saleId: Int, @Header("Authorization") userToken: String): Observable<Response<Void>>

    @GET("/sale/search/{query}")
    fun searchSale(@Path("query") query: String, @Header("Authorization") userToken: String): Observable<MutableList<Sale>>
}