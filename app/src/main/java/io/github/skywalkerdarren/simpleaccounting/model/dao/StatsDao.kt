package io.github.skywalkerdarren.simpleaccounting.model.dao

import androidx.room.Dao
import androidx.room.Query
import io.github.skywalkerdarren.simpleaccounting.model.entity.Stats
import io.github.skywalkerdarren.simpleaccounting.model.entity.TypeStats
import org.joda.time.DateTime
import java.util.*

@Dao
interface StatsDao {
    @Query(("SELECT type.is_expense as expense, sum(bill.balance) as balance " +
            "FROM bill INNER JOIN type " +
            "ON bill.type_id = type.uuid " +
            "WHERE bill.date BETWEEN :start AND :end " +
            "GROUP BY expense"))
    fun getBillsStats(start: DateTime, end: DateTime): List<Stats>?

    @Query(("SELECT type.uuid as type_id, sum(bill.balance) as balance " +
            "FROM bill INNER JOIN type " +
            "ON bill.type_id = type.uuid " +
            "WHERE type.is_expense = :isExpense AND bill.date BETWEEN :start AND :end " +
            "GROUP BY type_id " +
            "ORDER BY balance DESC"))
    fun getTypesStats(start: DateTime, end: DateTime, isExpense: Boolean): List<TypeStats>?

    @Query(("SELECT type.uuid as type_id, sum(bill.balance) as balance " +
            "FROM bill INNER JOIN type " +
            "ON bill.type_id = type.uuid " +
            "WHERE type.uuid = :typeId AND bill.date BETWEEN :start AND :end " +
            "GROUP BY type_id"))
    fun getTypeStats(start: DateTime, end: DateTime, typeId: UUID): TypeStats?

    @Query(("SELECT type.uuid as type_id, avg(bill.balance) as balance " +
            "FROM bill INNER JOIN type " +
            "ON bill.type_id = type.uuid " +
            "WHERE type.uuid = :typeId AND bill.date BETWEEN :start AND :end " +
            "GROUP BY type_id"))
    fun getTypeAverageStats(start: DateTime, end: DateTime, typeId: UUID): TypeStats?

    @Query(("SELECT type.is_expense as expense, sum(bill.balance) as balance " +
            "FROM bill INNER JOIN type INNER JOIN account " +
            "ON bill.account_id = account.uuid AND bill.type_id = type.uuid " +
            "WHERE account.uuid = :accountId AND bill.date BETWEEN :start AND :end " +
            "GROUP BY expense"))
    fun getAccountStats(accountId: UUID, start: DateTime, end: DateTime): List<Stats>?
}