package io.github.skywalkerdarren.simpleaccounting.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "currency_info", indices = @Index(value = "name", unique = true))
public final class CurrencyInfo implements Serializable {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "full_name")
    private String fullName;
    @ColumnInfo(name = "full_name_cn")
    private String fullNameCN;
    @ColumnInfo(name = "flag_location")
    private String flagLocation;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullNameCN() {
        return fullNameCN;
    }

    public void setFullNameCN(String fullNameCN) {
        this.fullNameCN = fullNameCN;
    }

    public String getFlagLocation() {
        return flagLocation;
    }

    public void setFlagLocation(String flagLocation) {
        this.flagLocation = flagLocation;
    }

    @Override
    public String toString() {
        return "currencyResource{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", fullNameCN='" + fullNameCN + '\'' +
                ", flagLocation='" + flagLocation + '\'' +
                '}';
    }
}
