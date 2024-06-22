package etf.rma.spirale.dataPersistence.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import etf.rma.spirale.biljka.Biljka
import etf.rma.spirale.biljka.BiljkaBitmap

@Database(entities = [Biljka::class, BiljkaBitmap::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun biljkaDao(): BiljkaDAO
    abstract fun biljkaBitmapDao(): BiljkaBitmapDAO
}