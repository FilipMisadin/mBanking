package com.example.mbanking.di.component

import android.app.Application
import com.example.mbanking.base.BaseApplication
import com.example.mbanking.di.modules.ActivityBindingModule
import com.example.mbanking.di.modules.ApplicationModule
import com.example.mbanking.di.modules.DatabaseModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, AndroidSupportInjectionModule::class, ActivityBindingModule::class, DatabaseModule::class])
interface ApplicationComponent : AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): ApplicationComponent
    }
}