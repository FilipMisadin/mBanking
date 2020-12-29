package com.example.mbanking.ui.transactions

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.mbanking.R
import com.example.mbanking.api.repository.BankingRepository
import com.example.mbanking.database.entities.*
import com.example.mbanking.ui.register.AccountDialogInteractor
import com.example.mbanking.utils.AppConstants.Companion.CURRENT_USER_PREFERENCE
import com.example.mbanking.utils.getPreferenceInt
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TransactionsViewModel
@Inject constructor(
    application: Application,
    private val repository: BankingRepository
) : AndroidViewModel(application), AccountDialogInteractor {
    private var disposable: CompositeDisposable?
    private lateinit var userWithAccounts: UserWithAccounts
    private var currentAccount: MutableLiveData<Account> = MutableLiveData()
    private var accountWithTransactions = MediatorLiveData<AccountWithTransactions>()

    private lateinit var lastSource: LiveData<AccountWithTransactions>

    override fun onCleared() {
        super.onCleared()
        if (disposable != null) {
            disposable!!.clear()
            disposable = null
        }
    }

    private fun getInitData() {
        //Get user id and all user accounts
        val userId = getPreferenceInt(getContext(), CURRENT_USER_PREFERENCE)
        disposable!!.add(repository.getUserAccounts(userId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<UserWithAccounts>() {
                override fun onError(e: Throwable) {
                    Toast.makeText(
                        getContext(), getContext().getString(R.string.accounts_loading_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onSuccess(user: UserWithAccounts) {
                    userWithAccounts = user
                    if(user.accounts.isEmpty())
                        return

                    //Set current account to first account in the list
                    currentAccount.postValue(user.accounts[0])
                    //Get transactions from first account in the list
                    getAccountWithTransactions(user.accounts[0].accountId)
                }
            })
        )
    }

    fun getAccounts(): UserWithAccounts {
        return userWithAccounts
    }

    fun getCurrentAccount(): LiveData<Account> {
        return currentAccount
    }

    fun getTransactions(): LiveData<AccountWithTransactions> {
        return accountWithTransactions
    }

    private fun getContext(): Context {
        return getApplication<Application>().applicationContext
    }

    @SuppressLint("CheckResult")
    private fun getAccountWithTransactions(currentAccountId: Long) {
        lastSource = repository.getAccountWithTransactions(currentAccountId)
        accountWithTransactions.addSource(lastSource) {
            //Sort transactions by date (descending)
            it.transactions = it.transactions.sortedWith(compareByDescending { it2 -> it2.date })
            accountWithTransactions.value = it
        }
    }

    init {
        disposable = CompositeDisposable()
        getInitData()
    }

    override fun onAccountSelected(account: Account) {
        //When user chooses new account, change data
        currentAccount.postValue(account)
        accountWithTransactions.removeSource(lastSource)
        getAccountWithTransactions(account.accountId)
    }
}