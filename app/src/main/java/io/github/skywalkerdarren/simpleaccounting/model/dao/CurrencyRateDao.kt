package io.github.skywalkerdarren.simpleaccounting.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import io.github.skywalkerdarren.simpleaccounting.model.entity.Currency
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyAndInfo

@Dao
interface CurrencyRateDao {
    @get:Query("SELECT * FROM currency")
    val currencies: LiveData<List<Currency>>

    @Query("SELECT r.name as r_name, r.favourite as r_favourite, r.exchange_rate as r_exchange_rate, r.source as r_source, r.id as r_id,\ni.id as i_id, i.name as i_name, i.full_name_cn as i_full_name_cn, i.full_name as i_full_name, i.flag_location as i_flag_location\nFROM currency r LEFT JOIN currency_info i ON i.name == r.name WHERE favourite == :favourite ORDER BY full_name_cn")
    fun getFavouriteCurrencies(favourite: Boolean): LiveData<List<CurrencyAndInfo>>

    @Query("SELECT * FROM currency WHERE name == :name")
    fun getCurrency(name: String): LiveData<Currency>

    @Query("SELECT * FROM currency WHERE name == :name")
    suspend fun getCurrencyRaw(name: String): Currency

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCurrency(currency: Currency)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCurrency(currency: Currency)

    @Query("DELETE FROM currency")
    suspend fun deleteCurrencies()

    @Query("UPDATE currency SET id = :id WHERE name == :name")
    suspend fun updateCurrencyId(name: String, id: Int)

    @Transaction
    suspend fun changeCurrency(currencyA: Currency, currencyB: Currency) {
        val a = getCurrencyRaw(currencyA.name).id
        val b = getCurrencyRaw(currencyB.name).id
        updateCurrencyId(currencyA.name, -1)
        updateCurrencyId(currencyB.name, a)
        updateCurrencyId(currencyA.name, b)
    }
}