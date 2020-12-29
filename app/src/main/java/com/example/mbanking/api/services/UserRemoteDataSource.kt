package com.example.mbanking.api.services

import com.example.mbanking.base.BaseDataSource
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userService: UserService
): BaseDataSource() {
    fun getUser() = userService.getUser()
}