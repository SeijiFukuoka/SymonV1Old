package br.com.symon.data.cache

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class CacheManagerImpl (context: Context, cacheName: String) : CacheManager {
    private var sharedPreferences: SharedPreferences? = null
    private val gSon = Gson()

    init {
        this.sharedPreferences = context.getSharedPreferences(cacheName, Context.MODE_PRIVATE)
    }

    override fun <T> get(key: String, clazz: Class<T>): T =
            gSon.fromJson(sharedPreferences?.getString(key, null), clazz)

    override fun <T> put(key: String, value: T) {
        val editor = sharedPreferences?.edit()
        editor?.putString(key, gSon.toJson(value))
        editor?.apply()
    }

    override fun contain(key: String): Boolean = sharedPreferences?.contains(key) ?: false

    override fun delete(key: String) {
        val editor = sharedPreferences?.edit()
        editor?.remove(key)
        editor?.apply()
    }
}