package io.github.skywalkerdarren.simpleaccounting.model.dao

import androidx.room.*
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill
import org.joda.time.DateTime
import java.util.*

@Dao
interface BillDao {
    @get:Query("SELECT count(id) FROM bill")
    val billsCount: Int

    @Query("SELECT count(id) FROM bill WHERE date BETWEEN :start AND :end")
    fun getBillsCount(start: DateTime, end: DateTime): Int

    @Query("SELECT * FROM bill WHERE uuid == :id")
    fun getBill(id: UUID): Bill?

    @Query("SELECT * FROM bill WHERE date BETWEEN :start AND :end ORDER BY date DESC")
    fun getsBillsByDate(start: DateTime, end: DateTime): List<Bill>?

    @Insert
    fun addBill(bill: Bill)

    @Query("DELETE FROM bill WHERE uuid = :uuid")
    fun delBill(uuid: UUID)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateBill(bill: Bill)

    @Query("DELETE FROM bill")
    fun clearBill()
}