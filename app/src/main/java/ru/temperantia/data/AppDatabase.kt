package ru.temperantia.data

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.House
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Redeem
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.SmartDisplay
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.ui.graphics.Color
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.temperantia.MyApplication
import ru.temperantia.R
import ru.temperantia.ui.theme.SoftGreen

@Database(entities = [Transaction::class, Category::class, Account::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun accountDao(): AccountDao
    companion object {
        private const val DATABASE_NAME = "temperantia_database"
        val instance: AppDatabase by lazy {
            Room.databaseBuilder(
                MyApplication.appContext,
                AppDatabase::class.java,
                DATABASE_NAME
            ).allowMainThreadQueries()
                .addCallback(DatabaseCallback(MyApplication.appContext))
                .build()
        }
    }
}

class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            insertInitialData()
        }
    }
    private fun insertInitialData() {
        val accountDao = AppDatabase.instance.accountDao()
        val defaultAccounts = listOf(
            Account(null, "accounts", context.getString(R.string.default_account_card), Icons.Outlined.CreditCard, Color(0xFF606fc1), false),
            Account(null, "accounts", context.getString(R.string.default_account_cash), Icons.Outlined.Payments, SoftGreen, false)
        )
        accountDao.insertAll(defaultAccounts)

        val categoryDao = AppDatabase.instance.categoryDao()
        val defaultCategories = listOf(
            Category(null, context.getString(R.string.default_category_groceries), Icons.Outlined.ShoppingCart, Color(0xFF40a0ed)),
            Category(null, context.getString(R.string.default_category_cafe), Icons.Outlined.Restaurant, Color(0xFF7a4dff)),
            Category(null, context.getString(R.string.default_category_home), Icons.Outlined.House, Color(0xFFffcd2c)),
            Category(null, context.getString(R.string.default_category_transport), Icons.Outlined.DirectionsBus, Color(0xFFfb8837)),
            Category(null, context.getString(R.string.default_category_leisure), Icons.Outlined.SmartDisplay, Color(0xFFef4b7d)),
            Category(null, context.getString(R.string.default_category_health), Icons.Outlined.VolunteerActivism, Color(0xFFff4342)),
            Category(null, context.getString(R.string.default_category_sport), Icons.Outlined.FitnessCenter, Color(0xFF67af45)),
            Category(null, context.getString(R.string.default_category_presents), Icons.Outlined.Redeem, Color(0xFFfe5dfb))
        )
        categoryDao.insertAll(defaultCategories)
    }
}