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

    override fun save(user: User) : Observable<Unit> = Observable.create<Unit> { emitter ->
        deleteUser()
        emitter.onNext(cacheManager?.put(CacheSettings.USER_KEY, user)!!)
        emitter.onComplete()
    }

    override fun getUser(): Observable<User> = Observable.create<User> { emitter ->
        var userCache : User? = cacheManager?.get(CacheSettings.USER_KEY, User::class.java)

        if (userCache == null)
            userCache = User()

        emitter.onNext(userCache)
        emitter.onComplete()
    }

    override fun deleteUser(): Observable<Void> = Observable.create {
        cacheManager?.delete(CacheSettings.USER_KEY)
    }
}
