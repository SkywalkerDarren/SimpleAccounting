package io.github.skywalkerdarren.simpleaccounting.util.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrenciesInfo;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Currency;

public class JsonConvertor {
    private static final GsonBuilder mBuilder = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation();

    private JsonConvertor() {

    }

    public static CurrenciesInfo toCurrenciesInfo(String json) {
        Gson gson = getGson();
        return gson.fromJson(json, CurrenciesInfo.class);
    }

    public static CurrenciesInfo toCurrenciesInfo(Reader json) {
        Gson gson = getGson();
        return gson.fromJson(json, CurrenciesInfo.class);
    }

    private static Gson getGson() {
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
        Type error = new TypeToken<Map<String, String>>() {
        }.getType();
        TypeAdapter<Map<String, String>> errorAdapter = new TypeAdapter<Map<String, String>>() {
            @Override
            public void write(JsonWriter out, Map<String, String> value) throws IOException {

            }

            @Override
            public Map<String, String> read(JsonReader in) throws IOException {
                Map<String, String> errorMap = new HashMap<>(3);
                in.beginObject();
                while (in.hasNext()) {
                    String key = in.nextName();
                    String value = in.nextString();
                    errorMap.put(key, value);
                }
                in.endObject();
                return errorMap;
            }
        };
        return mBuilder
                .registerTypeAdapter(currencyListType, currencyListReadAdapter)
                .registerTypeAdapter(error, errorAdapter).create();
    }

    public static Map<String, String> toCurrencyCodeMap(Reader json) {
        Gson gson = mBuilder.create();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

}
