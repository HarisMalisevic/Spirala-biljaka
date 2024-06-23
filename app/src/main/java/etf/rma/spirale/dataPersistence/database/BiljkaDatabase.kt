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
import androidx.room.Update
import etf.rma.spirale.App
import etf.rma.spirale.biljka.Biljka
import etf.rma.spirale.biljka.BiljkaBitmap
import etf.rma.spirale.dataPersistence.trefleAPI.TrefleDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
                context,
                BiljkaDatabase::class.java,
                "biljke-db"
            ).build()
        }
    }

    @Dao
    interface BiljkaDAO {
        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insert(biljka: Biljka): Long

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun saveBiljka(biljka: Biljka): Boolean {

            return withContext(Dispatchers.IO) {
                try {
                    biljka.id = insert(biljka)
                    true
                } catch (e: Exception) {
                    false
                }
            }
        }

        @Insert
        suspend fun insertAll(biljke: List<Biljka>)

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insert(biljkaBitmap: BiljkaBitmap): Long

        @Query("SELECT * FROM biljka WHERE id = :idBiljke")
        suspend fun getBiljka(idBiljke: Long): Biljka?

        @Query("SELECT * FROM BiljkaBitmap WHERE idBiljke = :idBiljke")
        suspend fun getBiljkaBitmap(idBiljke: Long): BiljkaBitmap?

        suspend fun addImage(idBiljke: Long, bitmap: Bitmap): Boolean {
            val biljka = getBiljka(idBiljke)
            val biljkaBitmap = getBiljkaBitmap(idBiljke)
            if (biljka == null || biljkaBitmap != null) {
                return false
            }
            val newBiljkaBitmap =
                BiljkaBitmap(null, idBiljke, Converters.BitmapConverter().fromBitmap(bitmap))
            val id = insert(newBiljkaBitmap)
            return id != -1L
        }

        @Query("SELECT * FROM biljka")
        suspend fun getAllBiljkas(): List<Biljka>

        @Query("DELETE FROM biljka")
        suspend fun clearBiljkaTable()

        @Query("DELETE FROM BiljkaBitmap")
        suspend fun clearBiljkaBitmapTable()

        suspend fun clearData() {
            clearBiljkaTable()
            clearBiljkaBitmapTable()
        }

        suspend fun getImageFromId(idBiljke: Long): Bitmap? {
            val biljkaBitmap = getBiljkaBitmap(idBiljke)
            return if (biljkaBitmap != null) {
                Converters.BitmapConverter().toBitmap(biljkaBitmap.bitmap)
            } else {
                null
            }
        }

        @Query("SELECT * FROM biljka where onlineChecked = 0")
        suspend fun getUncheckedBiljke(): List<Biljka>

        @Update
        suspend fun updateBiljke(biljke: List<Biljka>)

        suspend fun fixOfflineBiljka() : Int{
            val uncheckedBiljke = getUncheckedBiljke()
            val trefleDAO = TrefleDAO(App.context)
            for (biljka in uncheckedBiljke) {
                trefleDAO.fixData(biljka)
            }
            updateBiljke(uncheckedBiljke)
            return uncheckedBiljke.size
        }
    }
}