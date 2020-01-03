package io.github.skywalkerdarren.simpleaccounting.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.skywalkerdarren.simpleaccounting.model.dao.*
import io.github.skywalkerdarren.simpleaccounting.model.entity.*
import io.github.skywalkerdarren.simpleaccounting.util.TypeConvertUtil

@Database(entities = [Type::class, Account::class, Bill::class, Currency::class, CurrencyInfo::class], version = 2)
@TypeConverters(TypeConvertUtil::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun typeDao(): TypeDao
    abstract fun billDao(): BillDao
    abstract fun statsDao(): StatsDao
    abstract fun currencyRateDao(): CurrencyRateDao
    abstract fun currencyInfoDao(): CurrencyInfoDao

    companion object {
        private val lock = Any()
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            AppDatabase::class.java, "app.db")
                            // TODO: it will be removed
                            .fallbackToDestructiveMigration()
//                            .addMigrations(MigrationDb.MIGRATION_1_2)
                            .build()
                }
                return INSTANCE!!
            }
        }
    }
}