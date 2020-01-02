package io.github.skywalkerdarren.simpleaccounting.model.entity

import androidx.annotation.ColorRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

/**
 * 链式接口实现Type
 *
 * @author darren
 * @date 2018/3/31
 */
@Entity(tableName = "type", indices = [Index(value = ["uuid"], unique = true)])
data class Type @JvmOverloads constructor(
        @ColumnInfo(name = "name") var name: String,
        @ColumnInfo(name = "color_id") @ColorRes var colorId: Int,
        @ColumnInfo(name = "is_expense") var isExpense: Boolean = false,
        @ColumnInfo(name = "assets_name") var assetsName: String,
        @ColumnInfo(name = "uuid") var uuid: UUID = UUID.randomUUID()
) : Serializable {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}