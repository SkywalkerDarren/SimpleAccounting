package io.github.skywalkerdarren.simpleaccounting.model.dao;

import androidx.room.Dao;
import androidx.room.Query;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.AccountStats;
import io.github.skywalkerdarren.simpleaccounting.model.BillStats;
import io.github.skywalkerdarren.simpleaccounting.model.TypeStats;

@Dao
public interface StatsDao {
    @Query("")
    List<BillStats> getAnnualStats(int year);

    @Query("")
    List<BillStats> getMonthStats(int year, int month);

    @Query("")
    List<TypeStats> getTypeStats(DateTime start, DateTime end, boolean isExpense);

    @Query("")
    BigDecimal getTypeStats(DateTime start, DateTime end, UUID typeId);

    @Query("")
    List<AccountStats> getAccountStats(UUID accountId, int year);

    @Query("")
    AccountStats getAccountStats(UUID accountId, DateTime start, DateTime end);

    @Query("")
    BillStats getStats(DateTime start, DateTime end);

    @Query("")
    BigDecimal getTypeAverage(DateTime start, DateTime end, UUID typeId);
}
