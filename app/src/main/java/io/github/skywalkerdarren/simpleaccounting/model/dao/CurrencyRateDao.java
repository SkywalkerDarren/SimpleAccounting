package io.github.skywalkerdarren.simpleaccounting.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Currency;

@Dao
public interface CurrencyRateDao {
    @Query("SELECT * FROM currency")
    List<Currency> getCurrencies();

    @Query("SELECT * FROM currency WHERE favourite == :favourite")
    List<Currency> getFavouriteCurrencies(boolean favourite);

    @Query("SELECT * FROM currency WHERE name == :name")
    Currency getCurrency(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addCurrency(Currency currency);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCurrency(Currency currency);

    @Query("DELETE FROM currency WHERE name == :name")
    void deleteCurrency(String name);
}
