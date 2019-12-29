package io.github.skywalkerdarren.simpleaccounting.util;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CurrencyRequestTest {
    private static final String TAG = "CurrencyRequestTest";
    private Context mContext = ApplicationProvider.getApplicationContext();
    private CurrencyRequest mRequest;

    @Before
    public void init() {
        mRequest = new CurrencyRequest(mContext);
    }

    @Test
    public void constructorTest() {
        CurrencyRequest request = new CurrencyRequest(mContext);
        assertEquals("currencylayer_token", request.getTOKEN());
    }

    @Test
    public void getCurrencies() {
        List<Currency> currencies = mRequest.getCurrencies();
        assertNotNull(currencies);
        Log.d(TAG, "getCurrencies: " + currencies);
    }

    @Test
    public void checkConnection() {
        assertTrue(mRequest.checkConnection());
    }
}