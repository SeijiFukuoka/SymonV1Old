package br.com.symon.data.cache


interface CacheManager {
    fun <T> get(key: String, clazz: Class<T>): T
    fun <T> put(key: String, value: T)
    fun contain(key: String): Boolean
    fun delete(key: String)
}