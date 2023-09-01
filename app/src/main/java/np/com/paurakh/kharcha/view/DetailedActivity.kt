package np.com.paurakh.kharcha.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import np.com.paurakh.kharcha.Database.AppDatabase
import np.com.paurakh.kharcha.R
import np.com.paurakh.kharcha.model.Transaction

@Suppress("DEPRECATION")
class DetailedActivity : AppCompatActivity() {
    private var backButtonEnabled = false
    private lateinit var transaction:Transaction
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)

        val labelOfExp = findViewById<TextInputEditText>(R.id.titLabelInput)
        val amountOfExp = findViewById<TextInputEditText>(R.id.titAmountInput)
        val desOfExp = findViewById<TextInputEditText>(R.id.titDiscInput)
        val labelLayout = findViewById<TextInputLayout>(R.id.labelLayout)
        val amountLayout = findViewById<TextInputLayout>(R.id.tilAmountLayout)
        val disLayout = findViewById<TextInputLayout>(R.id.tilDiscLayout)
        val close_btn = findViewById<ImageButton>(R.id.ibCloseBtn)
        val rootView = findViewById<ConstraintLayout>(R.id.clRootView)

        val updateButton = findViewById<Button>(R.id.bUpdateTransactionBtn)

        transaction = intent.getSerializableExtra("transaction") as Transaction

        labelOfExp.setText(transaction.label)
        amountOfExp.setText(transaction.amount.toString())
        desOfExp.setText(transaction.description)

        rootView.setOnClickListener {
            this.window.decorView.clearFocus()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken,0)
        }


        labelOfExp.addTextChangedListener {
            updateButton.visibility = View.VISIBLE
            if(it!!.isNotEmpty()){
                labelLayout.error = null

            }
        }

        amountOfExp.addTextChangedListener{
            updateButton.visibility = View.VISIBLE
            if (it!!.isNotEmpty()){
                amountLayout.error = null
            }
        }

        desOfExp.addTextChangedListener{
            updateButton.visibility = View.VISIBLE
        }

        updateButton.setOnClickListener{
            val label = labelOfExp.text.toString()
            val desc = desOfExp.text.toString()
            val amount = amountOfExp.text.toString().toDoubleOrNull()
            if(label.isEmpty()){
                labelLayout.error = "Please Enter the valid label "

            }

            else if(amount==null){
                amountLayout.error = "Please Enter the valid amount "

            }
            else{
                val transaction = Transaction(transaction.id,label,amount,desc)
                update(transaction)
            }
        }
        close_btn.setOnClickListener {
            backButtonEnabled = true
            finish()
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (backButtonEnabled) {
            super.onBackPressed()
        }

    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun update(transaction: Transaction){
        val db = Room.databaseBuilder(this, AppDatabase::class.java,"transactions").build()

        GlobalScope.launch {
            db.transactionDao().update(transaction)
            finish()
        }
    }
}