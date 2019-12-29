package io.github.skywalkerdarren.simpleaccounting.model.entity;

import java.io.Serializable;

public class Currency implements Serializable {
    private String source;
    private String name;
    private Double exchangeRate;

    public Currency(String source, String name, Double exchangeRate) {
        this.source = source;
        this.name = name;
        this.exchangeRate = exchangeRate;
    }

    public String getSource() {
        return source;
    }

    public String getName() {
        return name;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "source='" + source + '\'' +
                ", name='" + name + '\'' +
                ", exchangeRate=" + exchangeRate +
                '}';
    }
}
