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
    private var loading: MutableLiveData<Boolean> = MutableLiveData()

    private fun fetchUser() {
        loading.postValue(true)

        //Get current user id from SharedPreference and use it to get user from database
        val userId = getPreferenceInt(getContext(), AppConstants.CURRENT_USER_PREFERENCE)
        user = repository.getUser(userId)

        loading.postValue(false)
    }

    fun getUser(): LiveData<User> {
        return user
    }

    private fun getContext(): Context {
        return getApplication<Application>().applicationContext
    }

    fun getLoading(): LiveData<Boolean> {
        return loading
    }

    init {
        fetchUser()
    }

    override fun onAuthentication() {
        loading.postValue(true)

        //If user gave correct pin, start transactions activity
        val transactionsActivity = Intent(getContext(), TransactionsActivity::class.java)
        transactionsActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        getContext().startActivity(transactionsActivity)

        loading.postValue(false)
    }
}