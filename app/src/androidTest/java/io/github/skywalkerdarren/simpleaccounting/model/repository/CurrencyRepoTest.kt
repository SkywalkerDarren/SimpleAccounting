package io.github.skywalkerdarren.simpleaccounting.model.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.skywalkerdarren.simpleaccounting.model.entity.Currency
import io.github.skywalkerdarren.simpleaccounting.model.entity.CurrencyInfo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CurrencyRepoTest {

    private val curA = Currency("AAA", "AAA", 1.0)
    private val curB = Currency("AAA", "BBB", 2.0)
    private val curC = Currency("AAA", "CCC", 0.5)
    private val infA = CurrencyInfo("AAA", "Aaa", "啊啊啊", "111")
    private val infB = CurrencyInfo("BBB", "Bbb", null, null)
    private val infC = CurrencyInfo("CCC", "Ccc", "从此次", "333")

    @Before
    fun setUp() {

    }

    @After
    fun tearDown() {

    }

    @Test
    fun updateCurrencies() {
    }

    @Test
    fun initCurrenciesAndInfos() {
    }

    @Test
    fun changeCurrencyPosition() {
    }

    @Test
    fun setCurrencyFav() {
    }

    @Test
    fun getCurrency() {
    }

    @Test
    fun getCurrencyExchangeRate() {
    }

    @Test
    fun getCurrenciesExchangeRate() {
    }

    @Test
    fun getFavouriteCurrenciesExchangeRate() {
    }

    @Test
    fun getCurrencyInfo() {
    }

    @Test
    fun getFavouriteCurrenciesInfo() {
    }

    @Test
    fun getCurrencyInfos() {
    }

    @Test
    fun getAllCurrencies() {
    }
}