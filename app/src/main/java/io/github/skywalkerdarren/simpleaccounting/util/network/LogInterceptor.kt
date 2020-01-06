package io.github.skywalkerdarren.simpleaccounting.util.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class LogInterceptor : Interceptor {
    private val TAG = "LogInterceptor"
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val t1 = System.nanoTime()
        Log.d(TAG, "Sending request on ${chain.connection()}\n" +
                "${request.headers}")

        val response = chain.proceed(request)

        val t2 = System.nanoTime()
        Log.d(TAG, "Received response in ${(t2 - t1) / 1e6}ms\n" +
                "${response.headers}")
        return response
    }

}