package etf.rma.spirale.dataPersistence.database

import android.content.Context
import android.graphics.Bitmap
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import etf.rma.spirale.biljka.Biljka
import etf.rma.spirale.biljka.BiljkaBitmap

@Dao
interface BiljkaDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(biljka: Biljka): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(biljkaBitmap: BiljkaBitmap): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveBiljka(biljka: Biljka): Boolean {
        val id = insert(biljka)
        return id != -1L
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImage(idBiljk: Long, bitmap: Bitmap): Boolean {
        val biljkaBitmap = BiljkaBitmap(idBiljk, Converters.bitmapToByteArray(bitmap))
        val id = insert(biljkaBitmap)
        return id != -1L
    }

    @Query("SELECT * FROM biljka")
    suspend fun getAllBiljkas(): List<Biljka>


    @Query("DELETE FROM biljka")
    suspend fun clearBiljkaTable()

    @Query("DELETE FROM biljka_bitmap")
    suspend fun clearBiljkaBitmapTable()

    suspend fun clearData() {
        clearBiljkaTable()
        clearBiljkaBitmapTable()
    }
}

@Database(entities = [Biljka::class, BiljkaBitmap::class], version = 1)
@TypeConverters(Converters::class)
abstract class BiljkaDatabase : RoomDatabase() {

    abstract fun biljkaDao(): BiljkaDAO

    companion object {
        @Volatile
        private var INSTANCE: BiljkaDatabase? = null

        fun getInstance(context: Context): BiljkaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = buildRoomDB(context)
                INSTANCE = instance
                instance
            }
        }

        private fun buildRoomDB(context: Context): BiljkaDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                BiljkaDatabase::class.java,
                "biljke-db"
            ).build()
        }
    }
}