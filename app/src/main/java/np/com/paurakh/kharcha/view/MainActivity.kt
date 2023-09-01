package np.com.paurakh.kharcha.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import np.com.paurakh.kharcha.Database.AppDatabase
import np.com.paurakh.kharcha.R
import np.com.paurakh.kharcha.model.Transaction
import np.com.paurakh.kharcha.view.transactionadapter.TransactionAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var  deletetedTransaction: Transaction
    private lateinit var transactions: List<Transaction>
    private lateinit var oldTransactions: List<Transaction>
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var linerLayoutManager: LayoutManager
    private lateinit var db: AppDatabase // Initialize this properly

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvView = findViewById<RecyclerView>(R.id.rvAllTransaction)
        val addTransactionBtn = findViewById<FloatingActionButton>(R.id.fabAddTransaction)
        transactions = arrayListOf()
        transactionAdapter = TransactionAdapter(transactions)
        linerLayoutManager = LinearLayoutManager(this)

        db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "transactions").build()

        rvView.apply {
            adapter = transactionAdapter
            layoutManager = linerLayoutManager
        }

        //swipe to remove

        val itemTouchHelper = object :ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteTransaction(transactions[viewHolder.adapterPosition])
            }

        }

        val swipeHelper  =ItemTouchHelper(itemTouchHelper)
        swipeHelper.attachToRecyclerView(rvView)


        addTransactionBtn.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchAll() {
        GlobalScope.launch {
           transactions = db.transactionDao().getAllListOfTransaction()

            runOnUiThread {
                updateDashBoard()
                transactionAdapter.setData(transactions)
            }
        }
    }


    private fun updateDashBoard(){
        //Find view by ID
        val mainTotalAmount = findViewById<TextView>(R.id.tvTotalAmount)
        val budget = findViewById<TextView>(R.id.tvBudget)
        val expenses = findViewById<TextView>(R.id.tvExpenses)

        //Work
        val totalAmount = transactions.map { it.amount }.sum()
        val budgetAmount = transactions.filter { it.amount>0 }.map { it.amount }.sum()
        val expenseAmount = totalAmount - budgetAmount

        mainTotalAmount.text = "रु%.2f".format(totalAmount)
        budget.text = "रु%.2f".format(budgetAmount)
        expenses.text = "रु%.2f".format(expenseAmount)
    }

    private fun undoDelete() {
        GlobalScope.launch {
            db.transactionDao().insert(deletetedTransaction)

            transactions = oldTransactions
            runOnUiThread {
                transactionAdapter.setData(transactions)
                updateDashBoard()
            }
        }
    }
    private fun showSnackBar() {
        val view =findViewById<View>(R.id.clMainCordinateLayout)
        val snackBar = Snackbar.make(view,"Transaction Deleted Successfully!",Snackbar.LENGTH_LONG)
        snackBar.setAction("Undo"){
            undoDelete()
        }
            .setActionTextColor(ContextCompat.getColor(this, R.color.red))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .show()


    }



    private fun deleteTransaction(transaction: Transaction){
        deletetedTransaction = transaction
        oldTransactions = transactions

        GlobalScope.launch {
            db.transactionDao().delete(transaction)
            transactions = transactions.filter { it.id != transaction.id }
            runOnUiThread {
                updateDashBoard()
                transactionAdapter.setData(transactions)
                showSnackBar()
            }
        }

    }



    override fun onResume() {
        super.onResume()
        fetchAll()
    }


}
