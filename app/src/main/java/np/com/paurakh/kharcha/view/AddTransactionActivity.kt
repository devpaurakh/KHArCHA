package np.com.paurakh.kharcha.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
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
class AddTransactionActivity : AppCompatActivity() {
    private var backButtonEnabled = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        val labelOfExp = findViewById<TextInputEditText>(R.id.titLabelInput)
        val amountOfExp = findViewById<TextInputEditText>(R.id.titAmountInput)
        val desOfExp = findViewById<TextInputEditText>(R.id.titDiscInput)
        val labelLayout = findViewById<TextInputLayout>(R.id.labelLayout)
        val amountLayout = findViewById<TextInputLayout>(R.id.tilAmountLayout)
        val disLayout = findViewById<TextInputLayout>(R.id.tilDiscLayout)
        val close_btn = findViewById<ImageButton>(R.id.ibCloseBtn)

        val addButton = findViewById<Button>(R.id.bAddTransactionBtn)

        labelOfExp.addTextChangedListener {
            if(it!!.isNotEmpty()){
                labelLayout.error = null

            }
        }

        amountOfExp.addTextChangedListener{
            if (it!!.isNotEmpty()){
                amountLayout.error = null
            }
        }

        addButton.setOnClickListener{
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
                val transaction = Transaction(0,label,amount,desc)
                insertData(transaction)
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
    private fun insertData(transaction: Transaction){
        val db = Room.databaseBuilder(this, AppDatabase::class.java,"transactions").build()

        GlobalScope.launch {
            db.transactionDao().insert(transaction)
            finish()
        }
    }

}