package com.example.mbanking.database.entities

import androidx.room.*
import com.example.mbanking.enums.TransactionType
import java.util.*

@Entity(primaryKeys = ["transactionId", "accountId" ],
    foreignKeys = [ForeignKey(entity = Account::class,
                parentColumns = arrayOf("accountId"),
                childColumns = arrayOf("accountId"),
                onDelete = ForeignKey.CASCADE
    )]
)
data class Transaction(
    val transactionId: Long,
    @ColumnInfo(index = true) val accountId: Long,
    val date: Date,
    val description: String,
    val amount: Double,
    val type: TransactionType? = null
)