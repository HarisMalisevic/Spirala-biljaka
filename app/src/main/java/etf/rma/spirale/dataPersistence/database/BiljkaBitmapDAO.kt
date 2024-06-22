package etf.rma.spirale.dataPersistence.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import etf.rma.spirale.biljka.BiljkaBitmap

@Dao
interface BiljkaBitmapDAO {
    @Insert
    suspend fun insert(biljkaBitmap: BiljkaBitmap): Long

    @Update
    suspend fun update(biljkaBitmap: BiljkaBitmap)

    @Delete
    suspend fun delete(biljkaBitmap: BiljkaBitmap)

    @Query("SELECT * FROM biljkaBitmap")
    suspend fun getAll(): List<BiljkaBitmap>

    @Query("SELECT * FROM biljkaBitmap WHERE idBiljke = :id")
    suspend fun getById(id: Int): BiljkaBitmap?
}