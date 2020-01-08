package io.github.skywalkerdarren.simpleaccounting.util.network

import android.content.Context
import android.content.pm.PackageManager
import androidx.preference.PreferenceManager
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrenciesInfo
import io.github.skywalkerdarren.simpleaccounting.util.data.JsonConvertor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RequestService {
    //Okhttp对象
    private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(LogInterceptor())
            .build()

    fun requestCurrenciesInfo(context: Context): Call<CurrenciesInfo> {
        // manifest 信息
        val info = context.packageManager.getApplicationInfo(
                context.packageName, PackageManager.GET_META_DATA)
        val baseToken = info.metaData.getString("com.currencylayer.TOKEN")

        val currenciesInfoGson = JsonConvertor.getCurrenciesInfoGson()
        val converter = GsonConverterFactory.create(currenciesInfoGson)

        val retrofit = Retrofit.Builder()
                .baseUrl(Address.apiLayer)
                .client(okHttpClient)
                .addConverterFactory(converter)
                .build()

        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val token = preferences.getString("token", baseToken)
                ?: throw IllegalArgumentException("no token found")

        return retrofit.create(RestfulApi::class.java).getCurrenciesInfo(token)
    }

}