package com.example.mbanking.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.example.mbanking.R
import com.example.mbanking.database.entities.Account
import com.example.mbanking.ui.register.AccountDialogInteractor

class AccountDialog(
    private val accounts: List<Account>,
    private val currentAccount: Account,
    private val dialogDismiss: AccountDialogInteractor
) : AppCompatDialogFragment() {
    @BindView(R.id.recyclerViewAccounts)
    lateinit var accountsRecyclerView: RecyclerView

    private lateinit var adapter: AccountListAdapter

    private var unbinder: Unbinder? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setTitle(R.string.choose_account)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_account, container, false)

        unbinder = ButterKnife.bind(this, view)

        setRecyclerView()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (unbinder != null) {
            unbinder!!.unbind()
            unbinder = null
        }
    }

    private fun setRecyclerView() {
        adapter = AccountListAdapter(
            accounts,
            currentAccount,
            context!!,
            this
        )
        accountsRecyclerView.adapter = adapter
        accountsRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    fun onAccountSelected(account: Account) {
        //When user selects account, call fun in ViewModel that changes account and transactions data
        dialogDismiss.onAccountSelected(account)
        dismiss()
    }
}