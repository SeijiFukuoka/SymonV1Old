package br.com.symon.data.cache

import android.content.Context
import br.com.symon.data.model.responses.UserTokenResponse
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserCacheManagerImpl @Inject constructor(context: Context) : UserCacheManager {

    private var cacheManager: CacheManager? = null

    init {
        cacheManager = CacheManagerImpl(context, CacheSettings.USER_CACHE_NAME)
    }

    override fun save(userTokenResponse: UserTokenResponse?): Observable<Unit> = Observable.create<Unit> { emitter ->
        deleteUser().subscribe {
            emitter.onNext(cacheManager?.put(CacheSettings.USER_KEY, userTokenResponse)!!)
            emitter.onComplete()
        }
    }

    override fun getUser(): Observable<UserTokenResponse> = Observable.create<UserTokenResponse> { emitter ->
        var userCache: UserTokenResponse? = cacheManager?.get(CacheSettings.USER_KEY, UserTokenResponse::class.java)

        if (userCache == null)
            userCache = UserTokenResponse(null, "")

        emitter.onNext(userCache)
        emitter.onComplete()
    }

    override fun deleteUser(): Observable<Unit?> = Observable.just(cacheManager?.delete(CacheSettings.USER_KEY))
}
