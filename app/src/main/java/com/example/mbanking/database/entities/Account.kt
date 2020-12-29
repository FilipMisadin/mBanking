package com.example.mbanking.database.entities

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.example.mbanking.enums.Currency

@Entity(foreignKeys = [ForeignKey(entity = User::class,
    parentColumns = arrayOf("userId"),
    childColumns = arrayOf("ownerId"),
    onDelete = CASCADE)],
    indices = [Index(value = ["iban"], unique = true)]
)
data class Account(
    @PrimaryKey val accountId: Long,
    @ColumnInfo(index = true) val ownerId: Long,
    val iban: String,
    val amount: Double,
    val currency: Currency
)