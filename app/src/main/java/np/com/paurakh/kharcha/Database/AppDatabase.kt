package np.com.paurakh.kharcha.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import np.com.paurakh.kharcha.model.Transaction

@Database(entities = [Transaction::class], version = 1)
abstract class AppDatabase:RoomDatabase() {
    abstract fun transactionDao():TransactionDao
}
