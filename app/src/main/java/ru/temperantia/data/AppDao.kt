package ru.temperantia.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions")
    fun getAll(): List<Transaction>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(transaction: Transaction)

    @Insert
    fun insertAll(transactions: List<Transaction>)

    @Delete
    fun delete(transaction: Transaction)

    @Query("DELETE FROM transactions")
    fun deleteAll()
}