package io.github.skywalkerdarren.simpleaccounting.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "currency", indices = [Index(value = ["name"], unique = true), Index(value = ["favourite"])])
data class Currency(@ColumnInfo(name = "source") var source: String,
                    @ColumnInfo(name = "name") var name: String,
                    @ColumnInfo(name = "exchange_rate") var exchangeRate: Double
) : Serializable {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    @ColumnInfo(name = "favourite")
    var favourite: Boolean = false
}