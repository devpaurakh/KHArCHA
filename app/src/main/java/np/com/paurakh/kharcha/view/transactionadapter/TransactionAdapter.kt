package np.com.paurakh.kharcha.view.transactionadapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import np.com.paurakh.kharcha.R
import np.com.paurakh.kharcha.model.Transaction
import np.com.paurakh.kharcha.view.DetailedActivity
import kotlin.math.abs

class TransactionAdapter(private  var transactions:List<Transaction>):RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactions[position]
        val context = holder.transactionAmount.context

        if(transaction.amount >= 0){
            holder.transactionAmount.text = "+रु %.1f".format(transaction.amount)
            holder.transactionAmount.setTextColor(ContextCompat.getColor(context,R.color.green))
        }else{
            holder.transactionAmount.text = "-रु %.1f".format(Math.abs(transaction.amount))
            holder.transactionAmount.setTextColor(ContextCompat.getColor(context,R.color.red))
        }
        holder.transactionLabel.text = transaction.label

        holder.itemView.setOnClickListener {
            val intent = Intent(context,DetailedActivity::class.java)
            intent.putExtra("transaction",transaction)
            context.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val transactionLabel:TextView = itemView.findViewById(R.id.tbTransactionLabel)
        val transactionAmount:TextView = itemView.findViewById(R.id.tbTransactionAmount)
    }

    fun setData(transactions: List<Transaction>){
        this.transactions = transactions
        notifyDataSetChanged()
    }

}