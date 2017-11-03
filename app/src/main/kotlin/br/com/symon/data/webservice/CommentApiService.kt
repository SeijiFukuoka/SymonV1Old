package br.com.symon.data.webservice

import br.com.symon.data.model.Comment
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import rx.Observable

interface CommentApiService {

  @GET("/sale/{sale_id}/comment")
  fun getComments(@Path("sale_id") saleId: Int): Observable<Response<List<Comment>>>

  @POST("/sale/{sale_id}/comment")
  @FormUrlEncoded
  fun postComment(@Body comment: Comment): Observable<Response<Comment>>

  @PUT("/comment/{comment_id}")
  @FormUrlEncoded
  fun updateComment(@Body comment: Comment): Observable<Response<Void>>

  @DELETE("/comment/{comment_id}")
  fun deleteComment(@Path("comment_id") commentId: Int): Observable<Response<Void>>
}