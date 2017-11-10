package br.com.symon.data.cache

import android.content.Context
import br.com.symon.data.model.User
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserCacheManagerImpl @Inject constructor(context: Context) : UserCacheManager {

    private var cacheManager: CacheManager? = null

    init {
        cacheManager = CacheManagerImpl(context, CacheSettings.USER_CACHE_NAME)
    }

    override fun save(user: User) : Observable<Unit> = Observable.create { subscriber ->
        deleteUser()
        cacheManager?.put(CacheSettings.USER_KEY, user)?.let { subscriber.onNext(it) }
        subscriber.onComplete()
    }


    override fun getUser(): Observable<User?> = Observable.just(cacheManager?.get(CacheSettings.USER_KEY, User::class.java))

    override fun deleteUser(): Observable<Void> = Observable.create { subscriber ->
        cacheManager?.delete(CacheSettings.USER_KEY)
    }
}