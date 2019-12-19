package io.github.skywalkerdarren.simpleaccounting.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill;

@Dao
public interface BillDao {
    @Query("SELECT * FROM bill WHERE uuid = :uuid")
    Bill getBill(UUID uuid);

    @Query("SELECT * FROM bill WHERE date BETWEEN :start AND :end ")
    List<Bill> getBills(Date start, Date end);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addBill(Bill bill);

    @Delete
    void deleteBill(Bill bill);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateBill(Bill bill);
}
