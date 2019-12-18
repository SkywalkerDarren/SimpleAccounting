package io.github.skywalkerdarren.simpleaccounting.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;

@Dao
public interface BillDao {
    @Query("SELECT * FROM bill WHERE uuid == :id")
    Bill getBill(UUID id);
    @Query("SELECT * FROM bill WHERE date BETWEEN :start AND :end")
    List<Bill> getsBillsByDate(DateTime start, DateTime end);
    @Insert
    void addBill(Bill bill);
    @Delete
    void delBill(Bill bill);
    @Update
    void updateBill(Bill bill);
}
