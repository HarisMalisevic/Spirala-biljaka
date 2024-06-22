package etf.rma.spirale.biljka

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "biljkaBitmap")
data class BiljkaBitmap(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "idBiljke") val idBiljke: Int = 0,
    @ColumnInfo(name = "bitmap", typeAffinity = ColumnInfo.BLOB) val bitmap: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BiljkaBitmap

        return idBiljke == other.idBiljke
    }

    override fun hashCode(): Int {
        return idBiljke
    }
}