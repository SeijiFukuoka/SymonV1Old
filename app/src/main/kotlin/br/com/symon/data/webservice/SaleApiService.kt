package br.com.symon.data.webservice

import br.com.symon.data.model.Sale
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import rx.Observable

interface SaleApiService {

  @GET("/sale")
  fun getSalesList(): Observable<Response<List<Sale>>>

  @GET("/sale/{sale_id}")
  fun getSaleDetail(@Path("sale_id") saleId: Int): Observable<Response<Sale>>

  @FormUrlEncoded
  @POST("/sale")
  fun postSaleMessage(@Body sale: Sale): Observable<Response<Sale>>

  @Multipart
  @POST("/sale/photo")
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