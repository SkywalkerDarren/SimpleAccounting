package io.github.skywalkerdarren.simpleaccounting.model.dao;

import androidx.room.Dao;
import androidx.room.Query;

import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.entity.Stats;
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeStats;

@Dao
public interface StatsDao {
    @Query("SELECT type.is_expense as expense, sum(bill.balance) as balance " +
            "FROM bill INNER JOIN type " +
            "ON bill.type_id = type.uuid " +
            "WHERE bill.date BETWEEN :start AND :end " +
            "GROUP BY expense")
    List<Stats> getBillsStats(DateTime start, DateTime end);

    @Query("SELECT type.uuid as type_id, sum(bill.balance) as balance " +
            "FROM bill INNER JOIN type " +
            "ON bill.type_id = type.uuid " +
            "WHERE type.is_expense = :isExpense AND bill.date BETWEEN :start AND :end " +
            "GROUP BY type_id " +
            "ORDER BY balance DESC")
    List<TypeStats> getTypesStats(DateTime start, DateTime end, boolean isExpense);

    @Query("SELECT type.uuid as type_id, sum(bill.balance) as balance " +
            "FROM bill INNER JOIN type " +
            "ON bill.type_id = type.uuid " +
            "WHERE type.uuid = :typeId AND bill.date BETWEEN :start AND :end " +
            "GROUP BY type_id")
    TypeStats getTypeStats(DateTime start, DateTime end, UUID typeId);

    @Query("SELECT type.uuid as type_id, avg(bill.balance) as balance " +
            "FROM bill INNER JOIN type " +
            "ON bill.type_id = type.uuid " +
            "WHERE type.uuid = :typeId AND bill.date BETWEEN :start AND :end " +
            "GROUP BY type_id")
    TypeStats getTypeAverageStats(DateTime start, DateTime end, UUID typeId);

    @Query("SELECT type.is_expense as expense, sum(bill.balance) as balance " +
            "FROM bill INNER JOIN type INNER JOIN account " +
            "ON bill.account_id = account.uuid AND bill.type_id = type.uuid " +
            "WHERE account.uuid = :accountId AND bill.date BETWEEN :start AND :end " +
            "GROUP BY expense")
    List<Stats> getAccountStats(UUID accountId, DateTime start, DateTime end);
}
