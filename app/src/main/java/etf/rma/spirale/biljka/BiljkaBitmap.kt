package etf.rma.spirale.biljka

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "biljka_bitmap")
data class BiljkaBitmap(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "idBiljke") val idBiljke: Long? = null,
    @ColumnInfo(name = "bitmap", typeAffinity = ColumnInfo.BLOB) val bitmap: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BiljkaBitmap

        return idBiljke == other.idBiljke
    }

    override fun hashCode(): Int {
        return (idBiljke ?: throw NullPointerException("Expression 'idBiljke' must not be null")).toInt()
    }
}