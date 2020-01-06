package io.github.skywalkerdarren.simpleaccounting.util.network

import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrenciesInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RestfulApi {
    @GET("api/live")
    fun getCurrenciesInfo(@Query("access_key") token: String): Call<CurrenciesInfo>
}