package io.github.skywalkerdarren.simpleaccounting.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo

@Dao
interface CurrencyInfoDao {
    @get:Query("SELECT * FROM currency_info ORDER BY full_name_cn")
    val infos: LiveData<List<CurrencyInfo>>

    @Query("SELECT * FROM currency_info WHERE name == :name")
    fun getInfo(name: String): LiveData<CurrencyInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addInfo(info: CurrencyInfo)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateInfo(info: CurrencyInfo)

    @Query("DELETE FROM currency_info WHERE name == :name")
    suspend fun deleteInfo(name: String)

    @get:Query("SELECT * FROM currency_info WHERE name IN (SELECT name FROM currency WHERE favourite == 1)")
    val favInfos: LiveData<List<CurrencyInfo>>
}