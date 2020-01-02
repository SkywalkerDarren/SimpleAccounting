package io.github.skywalkerdarren.simpleaccounting.model.entity

import androidx.annotation.ColorRes
import androidx.room.*
import java.math.BigDecimal
import java.util.*

/**
 * 存钱的账户
 *
 * @author darren
 * @date 2018/3/24
 */
@Entity(tableName = "account", indices = [Index(value = ["uuid"], unique = true)])
data class Account(@ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Int,
                   @ColumnInfo(name = "uuid") var uuid: UUID? = null,
                   @ColumnInfo(name = "name") var name: String? = null,
                   @ColumnInfo(name = "balance_hint") var balanceHint: String? = null,
                   @ColumnInfo(name = "balance") var balance: BigDecimal? = null,
                   @ColumnInfo(name = "image") var bitmap: String? = null,
                   @ColumnInfo(name = "color_id") @ColorRes var colorId: Int? = null

) {

    @Ignore
    constructor(uuid: UUID) : this(id = -1, uuid = uuid, name = null, balanceHint = null, balance = null, bitmap = null, colorId = null)

    constructor() : this(id = -1, uuid = UUID.randomUUID(), name = null, balanceHint = null, balance = null, bitmap = null, colorId = null)

    @Ignore
    constructor(name: String?, balanceHint: String?, balance: BigDecimal?, bitmap: String?, colorId: Int?) :
            this(id = -1, uuid = UUID.randomUUID(), name = name, balanceHint = balanceHint, balance = balance, bitmap = bitmap, colorId = colorId)

    fun plusBalance(balance: BigDecimal?) {
        this.balance = balance!!.add(balance)
    }

    fun minusBalance(balance: BigDecimal?) {
        this.balance = balance!!.subtract(balance)
    }

    companion object {
        @JvmField
        @Ignore
        val FOLDER = "account/"
    }
}