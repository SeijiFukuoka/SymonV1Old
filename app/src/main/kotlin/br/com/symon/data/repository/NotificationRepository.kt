package br.com.symon.data.repository

import br.com.symon.base.BaseRepository
import br.com.symon.data.webservice.NotificationApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(private val notificationApiService: NotificationApiService)
    : BaseRepository() {

    fun getNotifications(userToken: String) = call(notificationApiService.getNotifications(userToken))
}