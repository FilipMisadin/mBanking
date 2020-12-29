package com.example.mbanking.database.dao

import androidx.room.*
import com.example.mbanking.database.entities.Transaction

@Dao
interface TransactionDao{
    @Query("SELECT * FROM `Transaction`")
    fun getAll(): List<Transaction>

    @Query("SELECT * FROM `Transaction` WHERE transactionId LIKE :id")
    fun findById(id: Int): Transaction?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg transaction: Transaction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(transactions: List<Transaction>)

    @Delete
    fun delete(transaction: Transaction)

    @Update
    fun update(vararg transaction: Transaction)
}