package io.github.skywalkerdarren.simpleaccounting.util;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.VisibleForTesting;
import androidx.preference.PreferenceManager;

import java.io.IOException;

import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrenciesInfo;
import io.github.skywalkerdarren.simpleaccounting.util.data.JsonConvertor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static androidx.core.util.Preconditions.checkNotNull;

public class CurrencyRequest {
    private static final String TAG = "CurrencyRequest";
    private final String TOKEN;
    private String token;

    public CurrencyRequest(Context context) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            TOKEN = info.metaData.getString("com.currencylayer.TOKEN");
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            token = preferences.getString("token", TOKEN);
            if (TextUtils.isEmpty(token)) {
                token = TOKEN;
            }
            if (token == null) {
                throw new NullPointerException("Can't find your currencylayer token");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Can't find your PackageManager");
        }
    }

    @VisibleForTesting
    String getTOKEN() {
        return TOKEN;
    }

    public boolean checkConnection() {
        boolean isConnected = false;
        OkHttpClient client = new OkHttpClient();
        String url = "http://apilayer.net/api/live";
        Request request = new Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                isConnected = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isConnected;
    }

    public CurrenciesInfo getCurrenciesInfo() {
        if (!checkConnection()) {
            Log.e(TAG, "getCurrenciesInfo: checkConnection failed", new NetworkErrorException("Can't connect"));
            return null;
        }

        OkHttpClient client = new OkHttpClient();
        String url = "http://apilayer.net/api/live?access_key=" + token;
        Request request = new Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new NetworkErrorException("Can't connect");
            }
            ResponseBody body = response.body();
            checkNotNull(body);
            String json = body.string();
            return JsonConvertor.toCurrenciesInfo(json);
        } catch (IOException | NetworkErrorException e) {
            e.printStackTrace();
            Log.e(TAG, "getCurrenciesInfo: ", e);
            return null;
        }
    }
}
