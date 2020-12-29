package com.example.mbanking.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mbanking.database.entities.Account
import com.example.mbanking.database.entities.AccountWithTransactions

@Dao
interface AccountDao{
    @Query("SELECT * FROM Account")
    fun getAll(): List<Account>

    @Query("SELECT * FROM Account WHERE accountId LIKE :id")
    fun findById(id: Int): Account?

    @Transaction
    @Query("SELECT * FROM Account WHERE accountId LIKE :id")
    fun getAccountWithTransactions(id: Long): LiveData<AccountWithTransactions>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg account: Account)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(accounts: List<Account>)

    @Delete
    fun delete(account: Account)

    @Update
    fun update(vararg account: Account)
}