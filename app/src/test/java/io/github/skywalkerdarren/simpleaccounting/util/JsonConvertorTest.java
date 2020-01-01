package io.github.skywalkerdarren.simpleaccounting.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrenciesInfo;

@RunWith(JUnit4.class)
public class JsonConvertorTest {
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

    @Test
    public void toCurrencyInfo() {
        CurrenciesInfo info = JsonConvertor.toCurrenciesInfo(data);
        System.out.println("toCurrenciesInfo: " + info);
    }
}