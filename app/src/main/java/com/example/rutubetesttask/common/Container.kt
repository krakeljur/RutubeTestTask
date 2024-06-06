package com.example.rutubetesttask.common

import kotlinx.coroutines.runBlocking


sealed class Container<out T> {

    fun <R> map(mapper: ((T) -> R)? = null): Container<R> {
        return runBlocking {
            val suspendMapper: (suspend (T) -> R)? = if (mapper == null) {
                null
            } else {
                {
                    mapper(it)
                }
            }
            suspendMap(suspendMapper)
        }
    }

    abstract suspend fun <R>suspendMap(mapper: (suspend (T) -> R)? = null):Container<R>

    data class Success<out T>(val data: T) : Container<T>() {
        override suspend fun <R> suspendMap(mapper: (suspend (T) -> R)?): Container<R> {
            if (mapper == null) throw IllegalStateException("Mapper must be not null!!!")
            return try {
                Success(mapper(data))
            } catch (e: Exception) {
                Error(e.message ?: "UNKNOWN ERROR")
            }
        }
    }

    data class Error(val message: String) : Container<Nothing>() {
        override suspend fun <R> suspendMap(mapper: (suspend (Nothing) -> R)?): Container<R> {
            return this
        }

    }

    data object Pending : Container<Nothing>() {
        override suspend fun <R> suspendMap(mapper: (suspend (Nothing) -> R)?): Container<R> {
            return this
        }
    }

}