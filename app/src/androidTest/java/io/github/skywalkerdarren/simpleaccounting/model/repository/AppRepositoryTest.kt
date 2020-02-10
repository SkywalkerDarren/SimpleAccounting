package io.github.skywalkerdarren.simpleaccounting.model.repository

import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.adapter.BillAdapter
import io.github.skywalkerdarren.simpleaccounting.model.database.AppDatabase
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account
import io.github.skywalkerdarren.simpleaccounting.model.entity.Bill
import io.github.skywalkerdarren.simpleaccounting.model.entity.Type
import io.github.skywalkerdarren.simpleaccounting.util.SingleExecutors
import org.hamcrest.CoreMatchers.`is`
import org.joda.time.DateTime
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.math.BigDecimal

@RunWith(AndroidJUnit4::class)
class AppRepositoryTest {

    private val aA = Account("现金", "现金金额",
            BigDecimal.ZERO, R.color.amber500, "account/cash.png")
    private val aB = Account("支付宝", "在线支付余额",
            BigDecimal.ZERO, R.color.lightblue500, "account/alipay.png")
    private val aC = Account("微信", "在线支付余额",
            BigDecimal.ZERO, R.color.lightgreen500, "account/wechat.png")

    private val tA = Type("吃喝", R.color.diet, true, "type/diet.png")
    private val tB = Type("工资", R.color.wage, false, "type/wage.png")
    private val tC = Type("交通", R.color.traffic, true, "type/traffic.png")

    private val bA = Bill(tA.uuid, aA.uuid, DateTime(), tA.name, BigDecimal(1), null)
    private val bB = Bill(tA.uuid, aB.uuid, DateTime().minusDays(1), tA.name, BigDecimal(2), "test")
    private val bC = Bill(tB.uuid, aA.uuid, DateTime().minusMonths(1).minusDays(1), tB.name, BigDecimal(3), "")

    private lateinit var repo: AppRepository
    private lateinit var db: AppDatabase

    private fun assertAccount(actual: Account, except: Account) {
        assertThat(actual.name, `is`(except.name))
        assertThat(actual.balance, `is`(except.balance))
        assertThat(actual.balanceHint, `is`(except.balanceHint))
        assertThat(actual.bitmap, `is`(except.bitmap))
        assertThat(actual.colorId, `is`(except.colorId))
    }

    private fun assertType(actual: Type, except: Type) {
        assertThat(actual.name, `is`(except.name))
        assertThat(actual.assetsName, `is`(except.assetsName))
        assertThat(actual.colorId, `is`(except.colorId))
        assertThat(actual.isExpense, `is`(except.isExpense))
    }

    private fun assertBill(actual: Bill, except: Bill) {
        assertThat(actual.accountId, `is`(except.accountId))
        assertThat(actual.typeId, `is`(except.typeId))
        assertThat(actual.balance, `is`(except.balance))
        assertThat(actual.date, `is`(except.date))
        assertThat(actual.name, `is`(except.name))
        assertThat(actual.remark, `is`(except.remark))
    }

    private fun defaultInit() {
        db.accountDao().newAccount(aA)
        db.accountDao().newAccount(aB)
        db.typeDao().newType(tA)
        db.typeDao().newType(tB)
        db.billDao().addBill(bA)
        db.billDao().addBill(bB)
        db.billDao().addBill(bC)
    }

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
                getApplicationContext(), AppDatabase::class.java)
                .build()

        // clear
        AppRepository.clearInstance()
        repo = AppRepository.getInstance(SingleExecutors(), db)
    }

    @After
    fun tearDown() {
        db.close()
        AppRepository.clearInstance()
    }

    @Test
    fun getAccountsOnBackground() {
        db.accountDao().newAccount(aA)
        db.accountDao().newAccount(aB)
        db.accountDao().newAccount(aC)
        repo.getAccountsOnBackground here@{ accounts ->
            accounts ?: return@here fail()
            val a: Account = accounts.find { it.name == aA.name } ?: return@here fail()
            val b: Account = accounts.find { it.name == aB.name } ?: return@here fail()
            val c: Account = accounts.find { it.name == aC.name } ?: return@here fail()
            assertAccount(a, aA)
            assertAccount(b, aB)
            assertAccount(c, aC)
        }
    }

    @Test
    fun getBillInfoList() {
        defaultInit()
        val now = DateTime()
        repo.getBillInfoList(now.year, now.monthOfYear) here@{ list ->
            list ?: return@here fail()
            assertEquals(list.size, 4)
            val title = list.filter { it.type == BillAdapter.HEADER }.size
            val remark = list.filter { it.type == BillAdapter.WITH_REMARK }
            val noRemark = list.filter { it.type == BillAdapter.WITHOUT_REMARK }
            assertEquals(title, 2)
            assertEquals(remark.size, 1)
            assertEquals(noRemark.size, 1)
        }
    }

    @Test
    fun getAccount() {
        defaultInit()
        repo.getAccount(aA.uuid) { assertAccount(it, aA) }
    }

    @Test
    fun getsBills() {
        defaultInit()
        val current = DateTime()
        val lastMonth = DateTime().minusMonths(1)

        repo.run {
            getsBills(current.year, current.monthOfYear) { assertEquals(it?.size, 2) }
            getsBills(lastMonth.year, lastMonth.monthOfYear) { assertEquals(it?.size, 1) }
        }
    }

    @Test
    fun getAccounts() {
        defaultInit()
        repo.getAccounts { assertEquals(it?.size, 2) }
    }

    @Test
    fun updateAccountBalance() {
        defaultInit()
        repo.run {
            updateAccountBalance(aA.uuid, BigDecimal(233))
            getAccount(aA.uuid) { assertEquals(it.balance, BigDecimal(233)) }
        }
    }

    @Test
    fun delAccount() {
        defaultInit()
        repo.run {
            delAccount(aA.uuid)
            getAccounts { assertEquals(it?.size, 1) }
            getBillsCount { assertEquals(it, 1) }
        }
    }

    @Test
    fun changePosition() {
        defaultInit()
        repo.run {
            var a = aA
            var b = aB
            getAccount(aA.uuid) { a = it }
            getAccount(aB.uuid) { b = it }
            changePosition(a, b)
            getAccount(aA.uuid) { assertEquals(aB.id, aA.id) }
            getAccount(aB.uuid) { assertEquals(aB.id, aA.id) }
        }
    }

    @Test
    fun getBillsCount() {
        defaultInit()
        val current = DateTime()
        val lastMonth = DateTime().minusMonths(1)
        repo.run {
            getBillsCount(current.year, current.monthOfYear) { assertEquals(it, 2) }
            getBillsCount(lastMonth.year, lastMonth.monthOfYear) { assertEquals(it, 1) }
            getBillsCount { assertEquals(it, 3) }
        }
    }

    @Test
    fun getBill() {
        defaultInit()
        repo.getBill(bA.uuid) { assertEquals(it, bA) }
    }

    @Test
    fun addBill() {
        defaultInit()
        repo.run {
            val b1 = Bill(tB.uuid, aB.uuid, DateTime(), tB.name, BigDecimal(666), null)
            addBill(b1)
            getsBills(DateTime().year, DateTime().monthOfYear) {
                it?.forEach { b -> Log.d("test", b.toString()) }
            }
        }
    }

    @Test
    fun delBill() {
        defaultInit()
        repo.run {
            delBill(bA.uuid)
            getBillsCount { assertEquals(it, 2) }
        }
    }

    @Test
    fun updateBill() {
        defaultInit()
        repo.getBill(bA.uuid) {
            it ?: return@getBill fail()
            it.balance = BigDecimal(233)
            repo.updateBill(it)
        }
        repo.getBill(bA.uuid) {
            assertEquals(it?.balance, BigDecimal(233))
        }
    }

    @Test
    fun clearBill() {
        defaultInit()
        repo.clearBill()
        repo.getBillsCount { assertEquals(it, 0) }
    }

    @Test
    fun getType() {
    }

    @Test
    fun getTypes() {
    }

    @Test
    fun getTypesOnBackground() {
    }

    @Test
    fun delType() {
    }

    @Test
    fun getBillsAnnualStats() {
    }

    @Test
    fun getBillStats() {
    }

    @Test
    fun getTypesStats() {
    }

    @Test
    fun getTypeStats() {
    }

    @Test
    fun getTypeAverage() {
    }

    @Test
    fun getAccountStats() {
    }

    @Test
    fun initDb() {
    }
}