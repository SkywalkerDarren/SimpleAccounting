package io.github.skywalkerdarren.simpleaccounting.model.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class CurrencyInfo implements Serializable {
    @Expose
    private String success;
    @Expose
    private String timestamp;
    @Expose
    private List<Currency> quotes;

    public String getSuccess() {
        return success;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public List<Currency> getQuotes() {
        return quotes;
    }

    @Override
    public String toString() {
        return "CurrencyInfo{" +
                "success='" + success + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", quotes=" + quotes +
                '}';
    }
}
