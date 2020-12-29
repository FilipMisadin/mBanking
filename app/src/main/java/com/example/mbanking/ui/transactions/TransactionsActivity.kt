package com.example.mbanking.ui.transactions

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import com.example.mbanking.R
import com.example.mbanking.base.BaseActivity
import com.example.mbanking.ui.login.LoginActivity

class TransactionsActivity : BaseActivity() {
    override fun layoutRes(): Int {
        return R.layout.activity_transactions
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_ActionBar_Grey)
        super.onCreate(savedInstanceState)

        setActionBar()

        if (savedInstanceState == null)
            setDefaultFragment()
    }

    private fun setActionBar() {
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.toolbar_main)
        //supportActionBar?.elevation = 0F
        //OnClickListener for logout ImageButton in toolbar
        supportActionBar?.customView?.findViewById<ImageButton>(R.id.btnLogout)
            ?.setOnClickListener {
                showLogoutAlertDialog()
            }
    }

    private fun showLogoutAlertDialog() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialog))
        builder.setMessage(getString(R.string.logout_message))
        builder.setPositiveButton(R.string.yes) { _, _ ->
            //If user wants to logout, return him to login screen, and remove this screen
            startLoginActivity()
            finish()
        }
        builder.setNegativeButton(R.string.no) { _, _ -> }
        builder.show()
    }

    private fun startLoginActivity() {
        val loginActivity = Intent(this, LoginActivity::class.java)
        startActivity(loginActivity)
    }

    private fun setDefaultFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.screenContainer, TransactionsFragment()).commit()
    }
}