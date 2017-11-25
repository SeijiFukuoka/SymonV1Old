package br.com.symon.data.webservice

import br.com.symon.data.model.Sale
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface SaleApiService {

    @GET("/sale")
    fun getSalesList(): Observable<Response<List<Sale>>>

    @GET("/sale/{sale_id}")
    fun getSaleDetail(@Path("sale_id") saleId: Int): Observable<Response<Sale>>

    @FormUrlEncoded
    @POST("/sale")
    fun postSaleMessage(@Body sale: Sale): Observable<Response<Sale>>

    @Multipart
    @POST("/sale/photoUri")
    fun uploaSalePhoto(
            @Part photo: MultipartBody.Part,
            @Part("resource") name: RequestBody,
            @Field("sale_id") saleId: Int): Observable<Response<ResponseBody>>

    @FormUrlEncoded
    @PUT("/sale/{sale_id}")
    fun updateSale(@Body sale: Sale): Observable<Response<Void>>

    @DELETE("/sale/{sale_id}")
    fun deleteSale(@Path("sale_id") saleId: Int): Observable<Response<Void>>
}