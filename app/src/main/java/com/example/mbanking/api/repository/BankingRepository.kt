package com.example.mbanking.api.repository

import com.example.mbanking.api.responses.UserResponse
import com.example.mbanking.api.services.UserRemoteDataSource
import com.example.mbanking.database.dao.AccountDao
import com.example.mbanking.database.dao.TransactionDao
import com.example.mbanking.database.dao.UserDao
import com.example.mbanking.utils.userResponseToAccounts
import com.example.mbanking.utils.userResponseToTransactions
import com.example.mbanking.utils.userResponseToUser
import javax.inject.Inject

class BankingRepository @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val localUserDataSource: UserDao,
    private val localAccountDataSource: AccountDao,
    private val localTransactionDataSource: TransactionDao
) {
    fun fetchUser() = remoteDataSource.getUser()
    fun storeUserInDatabase(userResponse: UserResponse, firstName: String, lastName: String) {
        val user = userResponseToUser(userResponse, firstName, lastName)
        localUserDataSource.insert(user)

        val accounts = userResponseToAccounts(userResponse)
        localAccountDataSource.insertAll(accounts)

        val transactions = userResponseToTransactions(userResponse)
        localTransactionDataSource.insertAll(transactions)
    }

    fun getUserAccounts(id: Int) = localUserDataSource.getUserWithAccounts(id)
    fun getAccountWithTransactions(id: Long) =  localAccountDataSource.getAccountWithTransactions(id)
    fun getUser(id: Int) = localUserDataSource.findById(id)
}