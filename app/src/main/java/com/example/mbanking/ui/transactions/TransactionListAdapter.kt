package com.example.mbanking.ui.transactions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.mbanking.R
import com.example.mbanking.database.entities.Account
import com.example.mbanking.database.entities.Transaction
import com.example.mbanking.utils.dateToString

class TransactionListAdapter
internal constructor(
    viewModel: TransactionsViewModel, lifecycleOwner: LifecycleOwner,
    private val context: Context
) : RecyclerView.Adapter<TransactionListAdapter.HolesViewHolder>() {
    private val data: MutableList<Transaction> = mutableListOf()
    private lateinit var account: Account

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolesViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.item_transaction_list, parent, false)
        return HolesViewHolder(
            view,
            this
        )
    }

    override fun onBindViewHolder(holder: HolesViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class HolesViewHolder(itemView: View, private val adapter: TransactionListAdapter) :
        RecyclerView.ViewHolder(itemView) {
        @BindView(R.id.txtDescription) lateinit var descriptionText: TextView
        @BindView(R.id.txtType) lateinit var typeText: TextView
        @BindView(R.id.txtAmount) lateinit var amountText: TextView
        @BindView(R.id.txtDate) lateinit var dateText: TextView

        fun bind(transaction: Transaction) {
            descriptionText.text = transaction.description
            amountText.text = adapter.context.getString(
                R.string.transaction_amount,
                transaction.amount, adapter.account.currency
            )
            dateText.text = dateToString(transaction.date)
            if (transaction.type == null)
                typeText.text = ""
            else
                typeText.text = transaction.type.toString()
        }

        init {
            ButterKnife.bind(this, itemView)
        }
    }

    init {
        //Set observers for data change (changed when user chooses new account)
        viewModel.getTransactions()
            .observe(lifecycleOwner, androidx.lifecycle.Observer { accountWithTransactions ->
                data.clear()
                if (accountWithTransactions != null) {
                    data.addAll(accountWithTransactions.transactions)
                    notifyDataSetChanged()
                }
            })
        viewModel.getCurrentAccount()
            .observe(lifecycleOwner, androidx.lifecycle.Observer { currentAccount ->
                if (currentAccount != null) {
                    account = currentAccount
                }
            })
    }
}