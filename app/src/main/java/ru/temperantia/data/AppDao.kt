package ru.temperantia.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.Date

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions")
    fun getAll(): List<Transaction>

    @Query("SELECT MAX(date) FROM transactions")
    fun getLatestExpenseDate(): Date

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(transaction: Transaction)

    @Insert
    fun insertAll(transactions: List<Transaction>)

    @Delete
    fun delete(transaction: Transaction)

    @Query("DELETE FROM transactions")
    fun deleteAll()
}

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun getAll(): List<Category>

    @Query("SELECT * FROM categories WHERE id = :id")
    fun getById(id: Int): Category

    @Query(
        "SELECT * FROM categories " +
        "JOIN transactions ON categories.id = transactions.category_id"
    )
    fun loadCategoryAndTransactionList(): Map<Category, List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: Category)

    @Insert
    fun insertAll(categories: List<Category>)

    @Delete
    fun delete(category: Category)

    @Query("DELETE FROM categories")
    fun deleteAll()
}

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts")
    fun getAll(): List<Account>

    @Query("SELECT * FROM accounts WHERE id = :id")
    fun getById(id: Int): Account

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(account: Account)

    @Insert
    fun insertAll(accounts: List<Account>)

    @Delete
    fun delete(account: Account)

    @Query("DELETE FROM accounts")
    fun deleteAll()
}