package com.example.mbanking.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class AccountWithTransactions(
    @Embedded val account: Account,
    @Relation(
        parentColumn = "accountId",
        entityColumn = "accountId"
    )
    var transactions: List<Transaction>
)