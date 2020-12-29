package com.example.mbanking.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithAccounts(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "ownerId"
    )
    val accounts: List<Account>
)