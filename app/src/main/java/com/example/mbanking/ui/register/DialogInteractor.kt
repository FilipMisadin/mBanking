package com.example.mbanking.ui.register

import com.example.mbanking.database.entities.Account

interface PinDialogInteractor {
    fun onAuthentication()
}

interface AccountDialogInteractor {
    fun onAccountSelected(account: Account)
}