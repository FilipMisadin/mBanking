package com.example.mbanking.ui.register

import com.example.mbanking.di.modules.ViewModelModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class RegisterFragmentBindingModule {
    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun provideRegisterFragment(): RegisterFragment
}