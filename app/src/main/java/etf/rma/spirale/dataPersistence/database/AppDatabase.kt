package etf.rma.spirale.dataPersistence.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import etf.rma.spirale.biljka.Biljka
import etf.rma.spirale.biljka.BiljkaBitmap

@Database(entities = [Biljka::class, BiljkaBitmap::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun biljkaDao(): BiljkaDAO
    abstract fun biljkaBitmapDao(): BiljkaBitmapDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = buildRoomDB(context)
                INSTANCE = instance
                instance
            }
        }

        private fun buildRoomDB(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "biljke-db"
            ).build()
        }
    }
}