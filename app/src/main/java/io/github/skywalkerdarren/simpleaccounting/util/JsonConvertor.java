package io.github.skywalkerdarren.simpleaccounting.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Currency;
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo;

public class JsonConvertor {
    private final GsonBuilder mBuilder;

    public JsonConvertor() {
        mBuilder = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation();
    }

    public CurrencyInfo toCurrencyInfo(String json) {
        Type currencyListType = new TypeToken<List<Currency>>() {
        }.getType();
        TypeAdapter<List<Currency>> currencyListReadAdapter = new TypeAdapter<List<Currency>>() {
            @Override
            public void write(JsonWriter out, List<Currency> value) throws IOException {
            }

            @Override
            public List<Currency> read(JsonReader in) throws IOException {
                List<Currency> currencies = new ArrayList<>();
                in.beginObject();
                while (in.hasNext()) {
                    String name = in.nextName();
                    double rate = in.nextDouble();
                    currencies.add(new Currency(name.substring(0, 3), name.substring(3), rate));
                }
                in.endObject();
                return currencies;
            }
        };
        Gson gson = mBuilder.registerTypeAdapter(currencyListType, currencyListReadAdapter).create();
        return gson.fromJson(json, CurrencyInfo.class);
    }

}
