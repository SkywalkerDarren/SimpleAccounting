package io.github.skywalkerdarren.simpleaccounting.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo;

@RunWith(JUnit4.class)
public class JsonConvertorTest {
    private static final String TAG = "JsonConvertorTest";
    private final String data =
            "{\n" +
                    "  \"success\":true,\n" +
                    "  \"terms\":\"https:\\/\\/currencylayer.com\\/terms\",\n" +
                    "  \"privacy\":\"https:\\/\\/currencylayer.com\\/privacy\",\n" +
                    "  \"timestamp\":1577376547,\n" +
                    "  \"source\":\"USD\",\n" +
                    "  \"quotes\":{\n" +
                    "    \"USDAED\":3.67295,\n" +
                    "    \"USDAFN\":78.250053,\n" +
                    "    \"USDZWL\":322.000001\n" +
                    "  }\n" +
                    "}\n";

    private final String data2 =
            "{\"success\":true,\"terms\":\"aaaa\",\"privacy\":\"bbbb\",\"timestamp\":1577376547,\"source\":\"USD\",\"quotes\":{\"USDAED\":3.67295,\"USDAFN\":78.250053,\"USDZWL\":322.000001}}";
    private final String test = "{\"success\":true,\"terms\":\"aaaa\"}";

    @Test
    public void toCurrencyInfo() {
        JsonConvertor convertor = new JsonConvertor();
        CurrencyInfo info = convertor.toCurrencyInfo(data);
        System.out.println("toCurrencyInfo: " + info);
    }
}