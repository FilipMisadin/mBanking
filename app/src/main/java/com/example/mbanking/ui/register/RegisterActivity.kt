package com.example.mbanking.ui.register

import android.os.Bundle
import com.example.mbanking.R
import com.example.mbanking.base.BaseActivity

class RegisterActivity : BaseActivity() {
    override fun layoutRes(): Int {
        return R.layout.activity_register
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.NoActionBar)

        super.onCreate(savedInstanceState)

        if(savedInstanceState == null)
            setDefaultFragment()
    }

    private fun setDefaultFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.screenContainer, RegisterFragment()).commit()
    }
}