package com.example.mbanking.ui.transactions

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.example.mbanking.R
import com.example.mbanking.base.BaseFragment
import com.example.mbanking.database.entities.Account
import com.example.mbanking.database.entities.Transaction
import com.example.mbanking.ui.dialogs.AccountDialog
import com.example.mbanking.ui.transactions.RecyclerItemDecoration.SectionCallback
import com.example.mbanking.utils.ViewModelFactory
import com.example.mbanking.utils.dateToString
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class TransactionsFragment : BaseFragment(), View.OnClickListener {
    @BindView(R.id.txtAmount) lateinit var amountText: TextView
    @BindView(R.id.txtIban) lateinit var ibanText: TextView
    @BindView(R.id.btnAccounts) lateinit var accountsButton: Button

    @BindView(R.id.recyclerViewTransactions) lateinit var transactionsRecyclerView: RecyclerView

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var transactionsViewModel: TransactionsViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_transactions
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createViewModel()

        setOnClickListeners()

        setRecyclerView()

        setObservers()
    }

    private fun setObservers() {
        //On account changed, set toolbar title
        val title =
            baseActivity!!.supportActionBar!!.customView.findViewById<TextView>(R.id.txtTitleToolbar)
        transactionsViewModel.getTransactions()
            .observe(viewLifecycleOwner, Observer { accountWithTransactions ->
                title?.text = accountWithTransactions.account.iban
                amountText.text = getString(
                    R.string.amount_message,
                    accountWithTransactions.account.amount,
                    accountWithTransactions.account.currency
                )
                ibanText.text = accountWithTransactions.account.iban
            })
        //If user clicks on toolbar title, show dialog for choosing account
        title?.setOnClickListener {
            openDialog(transactionsViewModel.getCurrentAccount().value!!)
        }
    }

    private fun openDialog(currentAccount: Account) {
        val accountDialog = AccountDialog(
            transactionsViewModel.getAccounts().accounts,
            currentAccount, transactionsViewModel
        )
        accountDialog.show(activity!!.supportFragmentManager, "account dialog")
    }

    private fun setRecyclerView() {
        val adapter = TransactionListAdapter(transactionsViewModel, this, context!!)
        transactionsRecyclerView.adapter = adapter
        transactionsRecyclerView.layoutManager = LinearLayoutManager(context)

        //Set recycler item decoration (StickyHeader with date)
        val recyclerItemDecoration = RecyclerItemDecoration(
            context!!,
            resources.getDimensionPixelSize(R.dimen.header_height),
            true,
            getSectionCallback(transactionsViewModel, this)
        )
        transactionsRecyclerView.addItemDecoration(recyclerItemDecoration)
    }

    private fun getSectionCallback(
        viewModel: TransactionsViewModel,
        lifecycleOwner: LifecycleOwner
    ): SectionCallback {
        return object : SectionCallback {
            private val data: MutableList<Transaction> = mutableListOf()

            override fun isSection(pos: Int): Boolean {
                return pos == 0 || data[pos].date != data[pos - 1].date
            }

            override fun getSectionHeaderName(pos: Int): String {
                val dataMap = data[pos]
                return dateToString(dataMap.date)
            }

            init {
                //Set observer for updating data
                viewModel.getTransactions()
                    .observe(
                        lifecycleOwner,
                        androidx.lifecycle.Observer { accountWithTransactions ->
                            data.clear()
                            if (accountWithTransactions != null) {
                                data.addAll(accountWithTransactions.transactions)
                            }
                        })
            }
        }
    }

    private fun setOnClickListeners() {
        accountsButton.setOnClickListener(this)
    }

    private fun createViewModel() {
        transactionsViewModel = viewModelFactory.let {
            ViewModelProvider(this, it).get(TransactionsViewModel::class.java)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            accountsButton -> openDialog(transactionsViewModel.getCurrentAccount().value!!)
        }
    }
}