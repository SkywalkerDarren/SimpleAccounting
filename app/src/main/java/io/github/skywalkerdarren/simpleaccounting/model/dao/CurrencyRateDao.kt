package io.github.skywalkerdarren.simpleaccounting.model.dao

import androidx.room.*
import io.github.skywalkerdarren.simpleaccounting.model.entity.Currency

@Dao
interface CurrencyRateDao {
    @get:Query("SELECT * FROM currency")
    val currencies: List<Currency>?

    @Query("SELECT * FROM currency WHERE favourite == :favourite ORDER BY id DESC")
    fun getFavouriteCurrencies(favourite: Boolean): List<Currency>?

    @Query("SELECT * FROM currency WHERE name == :name")
    fun getCurrency(name: String): Currency?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCurrency(currency: Currency)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCurrency(currency: Currency)

    @Query("DELETE FROM currency WHERE name == :name")
    fun deleteCurrency(name: String)

    @Query("UPDATE currency SET id = :id WHERE name == :name")
    fun updateCurrencyId(name: String, id: Int)
}