package com.example.mbanking.ui.register

import android.app.Application
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mbanking.R
import com.example.mbanking.api.repository.BankingRepository
import com.example.mbanking.api.responses.UserResponse
import com.example.mbanking.database.entities.User
import com.example.mbanking.ui.transactions.TransactionsActivity
import com.example.mbanking.utils.AppConstants.Companion.CURRENT_USER_PREFERENCE
import com.example.mbanking.utils.AppConstants.Companion.REGISTER_PREFERENCE
import com.example.mbanking.utils.setPreference
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class RegisterViewModel
@Inject constructor(
    application: Application,
    private val repository: BankingRepository
) : AndroidViewModel(application), PinDialogInteractor {
    private var disposable: CompositeDisposable?
    private var user: User

    private var loading: MutableLiveData<Boolean> = MutableLiveData()
    val shouldCloseLiveData = MutableLiveData<Void>()

    override fun onCleared() {
        super.onCleared()
        if (disposable != null) {
            disposable!!.clear()
            disposable = null
        }
    }

    private fun fetchUser() {
        loading.postValue(true)
        disposable!!.add(
            repository.fetchUser().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UserResponse>() {
                    override fun onError(e: Throwable) {
                        Toast.makeText(
                            getContext(), getContext().getString(R.string.loading_error),
                            Toast.LENGTH_SHORT
                        ).show()
                        loading.postValue(false)
                    }

                    override fun onSuccess(userResponse: UserResponse) {
                        //When user is fetched from api, save it in local database
                        saveData(userResponse)
                    }
                })
        )
    }

    private fun saveData(userResponse: UserResponse) {
        disposable!!.add(Completable.fromAction {
            repository.storeUserInDatabase(
                userResponse,
                user.firstName,
                user.lastName
            )
        }.subscribeOn(Schedulers.io())
            .doOnComplete {
                //Set flag for registered user and user id
                setPreference(getContext(), REGISTER_PREFERENCE, true)
                setPreference(
                    getContext(),
                    CURRENT_USER_PREFERENCE,
                    userResponse.userId!!.toInt()
                )

                //Start transactions activity for registered user
                val transactionsActivity =
                    Intent(getContext(), TransactionsActivity::class.java)
                transactionsActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                getContext().startActivity(transactionsActivity)

                //Post value for removing activity observer
                shouldCloseLiveData.postValue(null)

                loading.postValue(false)
            }.doOnError {
                Toast.makeText(
                    getContext(), getContext().getString(R.string.saving_error),
                    Toast.LENGTH_SHORT
                ).show()
                loading.postValue(false)
            }.subscribe()
        )
    }

    fun getContext(): Context = getApplication<Application>().applicationContext

    fun setUser(firstName: String, lastName: String) {
        user.firstName = firstName
        user.lastName = lastName
    }

    fun getLoading() = loading

    override fun onAuthentication() {
        fetchUser()
    }

    init {
        disposable = CompositeDisposable()
        user = User(0, "", "")
    }
}