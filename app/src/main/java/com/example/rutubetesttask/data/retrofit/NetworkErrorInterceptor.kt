package com.example.rutubetesttask.data.retrofit

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import java.net.SocketTimeoutException

class NetworkErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            chain.proceed(chain.request())
        } catch (e: SocketTimeoutException) {
            Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(408)
                .message("No connection to server")
                .body(ResponseBody.create(null, ""))
                .build()
        } catch (e: Exception) {
            Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(500)
                .message("Unknown network error")
                .body(ResponseBody.create(null, ""))
                .build()
        }
    }
}