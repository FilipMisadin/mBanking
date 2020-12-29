package com.example.mbanking.ui.login

import com.example.mbanking.di.modules.ViewModelModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class LoginFragmentBindingModule {
    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun provideLoginFragment(): LoginFragment
}