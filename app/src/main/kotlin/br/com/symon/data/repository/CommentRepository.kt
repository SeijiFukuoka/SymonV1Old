package br.com.symon.data.repository

import br.com.symon.base.BaseRepository
import br.com.symon.data.webservice.CommentApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepository @Inject constructor(private val commentApiService: CommentApiService) :
        BaseRepository() {

    fun deleteComment(userToken: String, commentId: Int) = call(commentApiService.deleteComment(userToken, commentId))
}