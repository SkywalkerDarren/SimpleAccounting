package io.github.skywalkerdarren.simpleaccounting.model.entity

import androidx.room.*
import org.joda.time.DateTime
import java.io.Serializable
import java.math.BigDecimal
import java.util.*

/**
 * 账单类
 * 记录账单详细信息
 *
 * @author darren
 * @date 2018/1/29
 */
@Entity(tableName = "bill",
        indices = [
            Index(value = ["uuid"], unique = true),
            Index("type_id"), Index("account_id")],
        foreignKeys = [
            ForeignKey(entity = Type::class, parentColumns = ["uuid"], childColumns = ["type_id"],
                    onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
            ForeignKey(entity = Account::class, parentColumns = ["uuid"], childColumns = ["account_id"],
                    onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)])
data class Bill @JvmOverloads constructor(
        @ColumnInfo(name = "type_id") var typeId: UUID? = null,
        @ColumnInfo(name = "account_id") var accountId: UUID? = null,
        @ColumnInfo(name = "date") var date: DateTime? = null,
        @ColumnInfo(name = "name") var name: String? = null,
        @ColumnInfo(name = "balance") var balance: BigDecimal? = null,
        @ColumnInfo(name = "remark") var remark: String? = null,
        @ColumnInfo(name = "uuid") var uuid: UUID = UUID.randomUUID(),
        @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Int = 0
) : Serializable