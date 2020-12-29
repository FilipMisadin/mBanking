package com.example.mbanking.ui.login

import android.os.Bundle
import com.example.mbanking.R
import com.example.mbanking.base.BaseActivity

class LoginActivity : BaseActivity() {
    override fun layoutRes(): Int {
        return R.layout.activity_login
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.NoActionBar)

        super.onCreate(savedInstanceState)

        if (savedInstanceState == null)
            setDefaultFragment()
    }

    private fun setDefaultFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.screenContainer, LoginFragment()).commit()
    }
}