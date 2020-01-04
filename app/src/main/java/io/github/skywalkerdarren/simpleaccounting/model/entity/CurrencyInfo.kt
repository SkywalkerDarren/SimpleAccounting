package io.github.skywalkerdarren.simpleaccounting.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "currency_info", indices = [Index(value = ["name"], unique = true)])
data class CurrencyInfo @JvmOverloads constructor(
        @ColumnInfo(name = "name")
        var name: String,
        @ColumnInfo(name = "full_name")
        var fullName: String? = null,
        @ColumnInfo(name = "full_name_cn", collate = ColumnInfo.LOCALIZED)
        var fullNameCN: String? = null,
        @ColumnInfo(name = "flag_location")
        var flagLocation: String? = null
) : Serializable {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}