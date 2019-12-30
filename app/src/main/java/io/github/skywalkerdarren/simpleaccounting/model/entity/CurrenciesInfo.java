package io.github.skywalkerdarren.simpleaccounting.model.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CurrenciesInfo implements Serializable {
    @Expose
    private String success;
    @Expose
    private String timestamp;
    @Expose
    private List<Currency> quotes;
    @Expose
    private Map<String, String> error;

    public String getSuccess() {
        return success;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public List<Currency> getQuotes() {
        return quotes;
    }

    public Map<String, String> getError() {
        return error;
    }

    @Override
    public String toString() {
        return "CurrenciesInfo{" +
                "success='" + success + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", quotes=" + quotes +
                ", error=" + error +
                '}';
    }
}
