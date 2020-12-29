package com.example.mbanking.ui.login

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.*
import com.example.mbanking.api.repository.BankingRepository
import com.example.mbanking.database.entities.User
import com.example.mbanking.ui.register.PinDialogInteractor
import com.example.mbanking.ui.transactions.TransactionsActivity
import com.example.mbanking.utils.AppConstants
import com.example.mbanking.utils.getPreferenceInt
import javax.inject.Inject

class LoginViewModel
@Inject constructor(
    application: Application,
    private val repository: BankingRepository
) : AndroidViewModel(application), PinDialogInteractor {
    private lateinit var user: LiveData<User>

    private fun fetchUser() {
        //Get current user id from SharedPreference and use it to get user from database
        val userId = getPreferenceInt(getContext(), AppConstants.CURRENT_USER_PREFERENCE)
        user = repository.getUser(userId)
    }

    fun getUser(): LiveData<User> {
        return user
    }

    private fun getContext(): Context {
        return getApplication<Application>().applicationContext
    }

    init {
        fetchUser()
    }

    override fun onAuthentication() {
        //If user gave correct pin, start transactions activity
        val transactionsActivity = Intent(getContext(), TransactionsActivity::class.java)
        transactionsActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        getContext().startActivity(transactionsActivity)
    }
}