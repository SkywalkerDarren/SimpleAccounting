package io.github.skywalkerdarren.simpleaccounting.model.entity

import androidx.annotation.ColorRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import io.github.skywalkerdarren.simpleaccounting.R
import java.io.Serializable
import java.math.BigDecimal
import java.util.*

/**
 * 存钱的账户
 *
 * @author darren
 * @date 2018/3/24
 */
@Entity(tableName = "account", indices = [Index(value = ["uuid"], unique = true)])
data class Account @JvmOverloads constructor(
        @ColumnInfo(name = "name") var name: String,
        @ColumnInfo(name = "balance_hint") var balanceHint: String,
        @ColumnInfo(name = "balance") var balance: BigDecimal = BigDecimal.ZERO,
        @ColumnInfo(name = "color_id") @ColorRes var colorId: Int = R.color.white,
        @ColumnInfo(name = "image") var bitmap: String,
        @ColumnInfo(name = "uuid") var uuid: UUID = UUID.randomUUID(),
        @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Int = 0
) : Serializable {
    fun plusBalance(balance: BigDecimal) {
        this.balance = balance.add(balance)
    }

    fun minusBalance(balance: BigDecimal) {
        this.balance = balance.subtract(balance)
    }
}