package com.example.mbanking.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mbanking.database.entities.User
import com.example.mbanking.database.entities.UserWithAccounts
import io.reactivex.Single

@Dao
interface UserDao{
    @Query("SELECT * FROM User")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * FROM User WHERE userId LIKE :id")
    fun findById(id: Int): LiveData<User>

    @Transaction
    @Query("SELECT * FROM User WHERE userId LIKE :id")
    fun getUserWithAccounts(id : Int): Single<UserWithAccounts>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Delete
    fun delete(user: User)

    @Update
    fun update(vararg user: User)
}