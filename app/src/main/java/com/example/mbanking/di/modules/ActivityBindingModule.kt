package com.example.mbanking.di.modules

import com.example.mbanking.ui.login.LoginActivity
import com.example.mbanking.ui.login.LoginFragmentBindingModule
import com.example.mbanking.ui.register.RegisterActivity
import com.example.mbanking.ui.register.RegisterFragmentBindingModule
import com.example.mbanking.ui.transactions.TransactionsActivity
import com.example.mbanking.ui.transactions.TransactionsFragmentBindingModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {
    @ContributesAndroidInjector(modules = [RegisterFragmentBindingModule::class])
    abstract fun bindRegisterActivity(): RegisterActivity?

    @ContributesAndroidInjector(modules = [LoginFragmentBindingModule::class])
    abstract fun bindLoginActivity(): LoginActivity?

    @ContributesAndroidInjector(modules = [TransactionsFragmentBindingModule::class])
    abstract fun bindTransactionsActivity(): TransactionsActivity?
}