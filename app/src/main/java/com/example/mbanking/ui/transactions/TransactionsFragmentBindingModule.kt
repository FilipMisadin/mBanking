package com.example.mbanking.ui.transactions

import com.example.mbanking.di.modules.ViewModelModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class TransactionsFragmentBindingModule {
    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun provideTransactionsFragment(): TransactionsFragment
}