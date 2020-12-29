package com.example.mbanking.ui.start

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.mbanking.ui.login.LoginActivity
import com.example.mbanking.ui.register.RegisterActivity
import com.example.mbanking.utils.AppConstants
import com.example.mbanking.utils.getPreferenceBool

class StartActivity : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Launch a different activity
        val launchIntent = Intent()
        val launchActivity: Class<*>
        launchActivity = try {
            val className = getScreenClassName()
            Class.forName(className)
        } catch (e: ClassNotFoundException) {
            RegisterActivity::class.java
        }
        launchIntent.setClass(applicationContext, launchActivity)
        startActivity(launchIntent)
        finish()
    }

    private fun getScreenClassName(): String {
        //If user is registered start login activity, otherwise start register activity
        return if (getPreferenceBool(applicationContext, AppConstants.REGISTER_PREFERENCE)) {
            LoginActivity::class.java.name
        } else {
            RegisterActivity::class.java.name
        }
    }
}