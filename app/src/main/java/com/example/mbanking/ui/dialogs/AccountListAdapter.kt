package com.example.mbanking.ui.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.mbanking.R
import com.example.mbanking.database.entities.Account

class AccountListAdapter
internal constructor(
    private val data: List<Account>, private val currentAccount: Account,
    private val context: Context, private val owner: AccountDialog
) : RecyclerView.Adapter<AccountListAdapter.HolesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolesViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_account_list, parent, false)
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

    class HolesViewHolder(itemView: View, private val adapter: AccountListAdapter) :
        RecyclerView.ViewHolder(itemView) {
        @BindView(R.id.txtIban) lateinit var ibanText: TextView
        @BindView(R.id.txtAmount) lateinit var amountText: TextView
        @BindView(R.id.constraintLayoutItem) lateinit var itemLayout: ConstraintLayout

        private var account: Account? = null
        fun bind(account: Account) {
            this.account = account
            ibanText.text = account.iban
            amountText.text = adapter.context.getString(
                R.string.dash_separated_message,
                account.amount.toString(), account.currency
            )

            //If view account is already selected, change background color
            if (account == adapter.currentAccount) {
                itemLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        adapter.context,
                        R.color.colorAccent
                    )
                )
            }

            itemView.setOnClickListener {
                adapter.owner.onAccountSelected(account)
            }
        }

        init {
            ButterKnife.bind(this, itemView)
        }
    }

    init {
        setHasStableIds(true)
    }
}