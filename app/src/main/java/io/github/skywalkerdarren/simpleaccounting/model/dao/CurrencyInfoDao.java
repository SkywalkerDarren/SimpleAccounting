package io.github.skywalkerdarren.simpleaccounting.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo;

@Dao
public interface CurrencyInfoDao {
    @Query("SELECT * FROM currency_info")
    List<CurrencyInfo> getInfos();

    @Query("SELECT * FROM currency_info WHERE name == :name")
    CurrencyInfo getInfo(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addInfo(CurrencyInfo info);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateInfo(CurrencyInfo info);

    @Query("DELETE FROM currency_info WHERE name == :name")
    void deleteInfo(String name);
}
