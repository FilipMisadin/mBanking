package com.example.mbanking.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mbanking.database.dao.AccountDao
import com.example.mbanking.database.dao.TransactionDao
import com.example.mbanking.database.dao.UserDao
import com.example.mbanking.database.entities.*

@Database(entities = [Account::class, Transaction::class, User::class], version = 8, exportSchema = false)
@TypeConverters(Converters::class)
abstract class BankingDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var instance: BankingDatabase? = null

        fun getDatabase(context: Context): BankingDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, BankingDatabase::class.java, "bankDB")
                .fallbackToDestructiveMigration()
                .build()
    }
}