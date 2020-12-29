package com.example.mbanking.database

import android.util.Log
import androidx.room.TypeConverter
import com.example.mbanking.enums.Currency
import com.example.mbanking.enums.TransactionType
import java.util.*

class Converters {

    @TypeConverter
    fun toCurrency(value: String) = enumValueOf<Currency>(value)

    @TypeConverter
    fun fromCurrency(value: Currency) = value.name

    @TypeConverter
    fun toTransactionType(value: String?)
            = value?.let { enumValueOf<TransactionType>(it.replace(" ", "_")) }

    @TypeConverter
    fun fromTransactionType(value: TransactionType?) = value?.string

    @TypeConverter
    fun fromTimestamp(value: Long?) = value?.let { Date(it) }

    @TypeConverter
    fun toTimestamp(value: Date?) = value?.let { value.time }
}