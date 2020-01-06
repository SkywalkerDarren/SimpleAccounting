package io.github.skywalkerdarren.simpleaccounting.util;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrenciesInfo;
import io.github.skywalkerdarren.simpleaccounting.util.data.JsonConvertor;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class JsonConvertorTest {
    private static final String TAG = "JsonConvertorTest";
    private Context mContext = ApplicationProvider.getApplicationContext();


    @Test
    public void toCurrencyCodeCnMap() throws IOException {
        InputStream inputStream = mContext.getResources().getAssets().open("currency/translation_cn.json");
        Reader reader = new InputStreamReader(inputStream);
        Map<String, String> codeMap = JsonConvertor.toCurrencyCodeMap(reader);
        assertEquals("人民币元", codeMap.get("CNY"));
    }

    @Test
    public void toCurrencyCodeMap() throws IOException {
        InputStream inputStream = mContext.getResources().getAssets().open("currency/name.json");
        Reader reader = new InputStreamReader(inputStream);
        Map<String, String> codeMap = JsonConvertor.toCurrencyCodeMap(reader);
        assertEquals("Chinese Yuan", codeMap.get("CNY"));
    }


    @Test
    public void toCurrencyInfo() throws IOException {
        InputStream inputStream = mContext.getResources().getAssets().open("currency/default_rate.json");
        Reader reader = new InputStreamReader(inputStream);
        CurrenciesInfo info = JsonConvertor.toCurrenciesInfo(reader);
        assertEquals(167, info.getQuotes().size());
    }
}