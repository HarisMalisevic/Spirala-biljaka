package etf.rma.spirale.biljka

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "biljka_bitmap",
    foreignKeys = [
        ForeignKey(
            entity = Biljka::class,
            parentColumns = ["id"],
            childColumns = ["idBiljke"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BiljkaBitmap(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "idBiljke") val idBiljke: Long,
    @ColumnInfo(name = "bitmap") val bitmap: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BiljkaBitmap

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.toInt() ?: -1
    }
}