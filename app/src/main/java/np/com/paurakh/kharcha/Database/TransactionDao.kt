package np.com.paurakh.kharcha.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import np.com.paurakh.kharcha.model.Transaction

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions")
    fun getAllListOfTransaction():List<Transaction>

    @Insert
    fun insert(vararg transaction: Transaction)

    @Delete
    fun delete(transaction: Transaction)

    @Update
    fun update(vararg transaction: Transaction)
}