package io.github.skywalkerdarren.simpleaccounting.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type
import java.util.*

@Dao
interface TypeDao {
    @Query("SELECT * FROM type WHERE uuid == :uuid")
    fun getType(uuid: UUID): Type?

    @Query("SELECT * FROM type WHERE is_expense == :isExpense")
    fun getTypes(isExpense: Boolean): List<Type>?

    @Query("DELETE FROM type WHERE uuid = :uuid")
    fun delType(uuid: UUID)

    @Insert
    fun newType(type: Type)
}