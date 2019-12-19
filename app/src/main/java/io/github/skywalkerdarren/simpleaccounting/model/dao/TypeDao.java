package io.github.skywalkerdarren.simpleaccounting.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type;

@Dao
public interface TypeDao {
    @Query("SELECT * FROM type WHERE uuid == :uuid")
    Type getType(UUID uuid);
    @Query("SELECT * FROM type WHERE is_expense == :isExpense")
    List<Type> getTypes(Boolean isExpense);
    @Query("DELETE FROM type WHERE uuid = :uuid")
    void delType(UUID uuid);
    @Insert
    void newType(Type type);
}
