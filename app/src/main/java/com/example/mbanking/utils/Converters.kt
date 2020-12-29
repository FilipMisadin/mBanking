package com.example.mbanking.utils

import com.example.mbanking.api.responses.UserResponse
import com.example.mbanking.database.entities.Account
import com.example.mbanking.database.entities.Transaction
import com.example.mbanking.database.entities.User

fun userResponseToUser(userResponse: UserResponse, firstName: String, lastName: String): User {
    return User(userResponse.userId!!.toLong(), firstName, lastName)
}

fun userResponseToAccounts(userResponse: UserResponse): List<Account> {
    val accounts = mutableListOf<Account>()
    for (account in userResponse.accounts!!) {
        accounts.add(
            Account(
                account!!.id.toLong(), userResponse.userId!!.toLong(),
                account.iban, stringToDouble(account.amount), account.currency!!
            )
        )
    }

    return accounts
}

fun userResponseToTransactions(userResponse: UserResponse): List<Transaction> {
    val transactions = mutableListOf<Transaction>()
    for (account in userResponse.accounts!!) {
        for (transaction in account!!.transactions!!) {
            transactions.add(
                Transaction(
                    transaction!!.id.toLong(),
                    account.id.toLong(),
                    stringToDate(transaction.date)!!,
                    transaction.description,
                    stringToDouble(transaction.amount),
                    transaction.type
                )
            )
        }
    }

    return transactions
}