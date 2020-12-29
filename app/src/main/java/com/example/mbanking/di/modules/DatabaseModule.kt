package com.example.mbanking.di.modules

import android.content.Context
import com.example.mbanking.database.BankingDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = BankingDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideUserDao(db: BankingDatabase) = db.userDao()

    @Singleton
    @Provides
    fun provideAccountDao(db: BankingDatabase) = db.accountDao()

    @Singleton
    @Provides
    fun provideTransactionDao(db: BankingDatabase) = db.transactionDao()
}