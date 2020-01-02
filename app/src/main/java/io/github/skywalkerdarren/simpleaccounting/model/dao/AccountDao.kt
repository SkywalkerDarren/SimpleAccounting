package io.github.skywalkerdarren.simpleaccounting.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account
import java.util.*

@Dao
interface AccountDao {
    @Query("SELECT * FROM account WHERE uuid == :uuid")
    fun getAccount(uuid: UUID): Account

    @get:Query("SELECT * FROM account ORDER BY id")
    val accounts: List<Account>

    @Query("UPDATE OR REPLACE account SET id = :id WHERE uuid = :uuid")
    fun updateAccountId(uuid: UUID, id: Int)

    @Query("DELETE FROM account WHERE uuid = :uuid")
    fun delAccount(uuid: UUID)

    @Insert
    fun newAccount(account: Account)
}