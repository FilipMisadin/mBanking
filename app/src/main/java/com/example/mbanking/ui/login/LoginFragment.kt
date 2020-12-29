package com.example.mbanking.ui.login

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import com.example.mbanking.R
import com.example.mbanking.base.BaseFragment
import com.example.mbanking.ui.dialogs.PinDialog
import com.example.mbanking.utils.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : BaseFragment(), View.OnClickListener {
    @BindView(R.id.btnLogin) lateinit var loginButton: Button

    @BindView(R.id.txtName) lateinit var nameText: TextView

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var loginViewModel: LoginViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_login
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createViewModel()

        setObservers()

        setOnClickListeners()
    }

    private fun setObservers() {
        loginViewModel.getUser().observe(viewLifecycleOwner, Observer { user ->
            nameText.text = getString(
                R.string.dash_separated_message, user.firstName,
                user.lastName
            )
        })
    }

    private fun setOnClickListeners() {
        btnLogin.setOnClickListener(this)
    }

    private fun createViewModel() {
        loginViewModel = viewModelFactory.let {
            ViewModelProvider(this, it).get(LoginViewModel::class.java)
        }
    }

    private fun openDialog() {
        val pinDialog =
            PinDialog(loginViewModel, false)
        pinDialog.show(activity!!.supportFragmentManager, "pin dialog")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            btnLogin -> openDialog()
        }
    }
}