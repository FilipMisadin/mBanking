package com.example.mbanking.ui.register

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import com.example.mbanking.R
import com.example.mbanking.base.BaseFragment
import com.example.mbanking.ui.dialogs.PinDialog
import com.example.mbanking.utils.ViewModelFactory
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class RegisterFragment : BaseFragment(), View.OnClickListener {
    @BindView(R.id.btnRegister) lateinit var registerButton: Button
    @BindView(R.id.textInputFirstName) lateinit var firstNameTextInput: TextInputLayout
    @BindView(R.id.textInputLastName) lateinit var lastNameTextInput: TextInputLayout

    @BindView(R.id.layoutProgressBar) lateinit var progressBarLayout: FrameLayout

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var registerViewModel: RegisterViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_register
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createViewModel()

        setOnClickListeners()

        setObservers()
    }

    private fun setObservers() {
        //Close activity when user finishes registration
        val liveData = registerViewModel.shouldCloseLiveData
        liveData.observe(viewLifecycleOwner, Observer {
            activity!!.finish()
            liveData.removeObservers(this)
        })

        //Show progress bar when loading
        registerViewModel.getLoading().observe(viewLifecycleOwner, Observer { loading ->
            if (loading)
                progressBarLayout.visibility = View.VISIBLE
            else
                progressBarLayout.visibility = View.INVISIBLE
        })

        //Subscribe to first name and last name value change, check if they are empty
        val firstNameTextChange = RxTextView.textChanges(firstNameTextInput.editText!!)
        val lastNameTextChange = RxTextView.textChanges(lastNameTextInput.editText!!)
        addToDisposable(Observable.combineLatest(listOf(firstNameTextChange, lastNameTextChange)) {
            val firstName = it[0].toString()
            val lastName = it[1].toString()

            //If first name or last name is empty disable register button
            registerButton.isEnabled = firstName.length in 1..30 && lastName.length in 1..30
            registerViewModel.setUser(firstName, lastName)
        }.subscribeOn(AndroidSchedulers.mainThread()).subscribe())
    }

    private fun setOnClickListeners() {
        registerButton.setOnClickListener(this)
    }

    private fun createViewModel() {
        registerViewModel = viewModelFactory.let {
            ViewModelProvider(this, it).get(RegisterViewModel::class.java)
        }
    }

    private fun openDialog() {
        val pinDialog = PinDialog(registerViewModel, true)
        pinDialog.show(activity!!.supportFragmentManager, "Pin dialog")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            registerButton -> openDialog()
        }
    }
}