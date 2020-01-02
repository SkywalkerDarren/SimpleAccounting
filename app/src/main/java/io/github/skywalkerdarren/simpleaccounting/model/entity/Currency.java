package io.github.skywalkerdarren.simpleaccounting.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "currency", indices = {@Index(value = "name", unique = true), @Index(value = "favourite")})
public final class Currency implements Serializable {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @ColumnInfo(name = "source")
    private String source;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "exchange_rate")
    private Double exchangeRate;
    @ColumnInfo(name = "favourite")
    private Boolean favourite;

    public Currency(String source, String name, Double exchangeRate) {
        this.source = source;
        this.name = name;
        this.exchangeRate = exchangeRate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", source='" + source + '\'' +
                ", name='" + name + '\'' +
                ", exchangeRate=" + exchangeRate +
                ", favourite=" + favourite +
                '}';
    }
}
