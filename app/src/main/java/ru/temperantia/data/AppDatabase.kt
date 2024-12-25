package ru.temperantia.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.temperantia.MyApplication

@Database(entities = [Transaction::class, Category::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    companion object {
        private const val DATABASE_NAME = "temperantia_database"
        val instance: AppDatabase by lazy {
            Room.databaseBuilder(
                MyApplication.appContext,
                AppDatabase::class.java,
                DATABASE_NAME
            ).allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}