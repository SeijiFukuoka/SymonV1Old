package br.com.symon.data.webservice

import br.com.symon.data.model.Notification
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header


interface NotificationApiService {

    @GET("/interaction")
    fun getNotifications(@Header("Authorization") userToken: String): Observable<MutableList<Notification>>
}