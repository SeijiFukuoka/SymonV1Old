package br.com.symon.data.cache

import br.com.symon.data.model.User
import io.reactivex.Observable


interface UserCacheManager {
    fun save(user: User): Observable<Unit>

    fun getUser(): io.reactivex.Observable<User>

    fun deleteUser(): Observable<Void>
}