package br.com.symon.data.cache

import br.com.symon.data.model.User


interface UserCacheManager {
    fun save(user: User): io.reactivex.Observable<Unit>

    fun getUser(): io.reactivex.Observable<User>

    fun deleteUser(): io.reactivex.Observable<Void>
}