package ru.temperantia.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.temperantia.MyApplication

@Database(entities = [Transaction::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
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