package br.com.symon.data.cache

import br.com.symon.data.model.responses.UserTokenResponse


interface UserCacheManager {
    fun save(user: UserTokenResponse?): io.reactivex.Observable<Unit>

    fun getUser(): io.reactivex.Observable<UserTokenResponse>

    fun deleteUser(): io.reactivex.Observable<Void>
}