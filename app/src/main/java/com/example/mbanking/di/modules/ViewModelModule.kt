package com.example.mbanking.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mbanking.di.util.ViewModelKey
import com.example.mbanking.ui.login.LoginViewModel
import com.example.mbanking.ui.register.RegisterViewModel
import com.example.mbanking.ui.transactions.TransactionsViewModel
import com.example.mbanking.utils.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(priceViewModel: RegisterViewModel?): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(priceViewModel: LoginViewModel?): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TransactionsViewModel::class)
    abstract fun bindTransactionsViewModel(priceViewModel: TransactionsViewModel?): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory?): ViewModelProvider.Factory
}