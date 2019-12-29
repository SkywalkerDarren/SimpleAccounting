package io.github.skywalkerdarren.simpleaccounting.util;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.VisibleForTesting;

import java.io.IOException;
import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Currency;
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static androidx.core.util.Preconditions.checkNotNull;

public class CurrencyRequest {
    private static final String TAG = "CurrencyRequest";
    private final String TOKEN;

    public CurrencyRequest(Context context) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            TOKEN = info.metaData.getString("com.currencylayer.TOKEN");
            if (TOKEN == null) {
                throw new NullPointerException("Can't find your currencylayer token");
            }
            Log.d(TAG, "CurrencyRequest: " + TOKEN);
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

    public List<Currency> getCurrencies() {
        if (!checkConnection()) {
            return null;
        }

        OkHttpClient client = new OkHttpClient();
        String url = "http://apilayer.net/api/live?access_key=" + TOKEN;
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
            JsonConvertor convertor = new JsonConvertor();
            CurrencyInfo info = convertor.toCurrencyInfo(json);
            if ("true".equals(info.getSuccess())) {
                return info.getQuotes();
            } else {
                return null;
            }
        } catch (IOException | NetworkErrorException e) {
            e.printStackTrace();
            return null;
        }
    }
}
