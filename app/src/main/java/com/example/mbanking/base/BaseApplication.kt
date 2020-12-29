package com.example.mbanking.base

import com.example.mbanking.di.component.ApplicationComponent
import com.example.mbanking.di.component.DaggerApplicationComponent
import dagger.android.support.DaggerApplication

class BaseApplication : DaggerApplication() {
    override fun applicationInjector(): ApplicationComponent? {
        val component: ApplicationComponent? =
            DaggerApplicationComponent.builder().application(this).build()
        component?.inject(this)
        return component
    }
}