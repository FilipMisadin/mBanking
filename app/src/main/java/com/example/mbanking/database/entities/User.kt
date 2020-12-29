package com.example.mbanking.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val userId: Long,
    var firstName: String,
    var lastName: String
)