package io.github.skywalkerdarren.simpleaccounting.model.dao

import androidx.room.*
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo

@Dao
interface CurrencyInfoDao {
    @get:Query("SELECT * FROM currency_info ORDER BY full_name_cn")
    val infos: List<CurrencyInfo>?

    @Query("SELECT * FROM currency_info WHERE name == :name")
    fun getInfo(name: String): CurrencyInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addInfo(info: CurrencyInfo)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateInfo(info: CurrencyInfo)

    @Query("DELETE FROM currency_info WHERE name == :name")
    fun deleteInfo(name: String)
}