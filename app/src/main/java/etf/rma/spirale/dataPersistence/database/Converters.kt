package etf.rma.spirale.dataPersistence.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import etf.rma.spirale.biljka.KlimatskiTip
import etf.rma.spirale.biljka.MedicinskaKorist
import etf.rma.spirale.biljka.Zemljiste
import java.io.ByteArrayOutputStream

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromMedicinskaKoristList(value: List<MedicinskaKorist>): String {
        val type = object : TypeToken<List<MedicinskaKorist>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toMedicinskaKoristList(value: String): List<MedicinskaKorist> {
        val type = object : TypeToken<List<MedicinskaKorist>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromKlimatskiTipList(value: List<KlimatskiTip>): String {
        val type = object : TypeToken<List<KlimatskiTip>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toKlimatskiTipList(value: String): List<KlimatskiTip> {
        val type = object : TypeToken<List<KlimatskiTip>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromZemljisteList(value: List<Zemljiste>): String {
        val type = object : TypeToken<List<Zemljiste>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toZemljisteList(value: String): List<Zemljiste> {
        val type = object : TypeToken<List<Zemljiste>>() {}.type
        return gson.fromJson(value, type)
    }

    companion object {
        fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
            return stream.toByteArray()

        }

        fun byteArrayToBitmap(bitmap: ByteArray): Bitmap {
            return bitmap.size.let { BitmapFactory.decodeByteArray(bitmap, 0, it) }

        }
    }


    class BitmapConverter {
        @TypeConverter
        fun fromBitmap(bitmap: Bitmap): String {
            val resizedBitmap = resizeBitmap(bitmap)
            val outputStream = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
            return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        }

        @TypeConverter
        fun toBitmap(data: String?): Bitmap {
            val bytes: ByteArray = Base64.decode(data, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }

        companion object {
            private fun resizeBitmap(bitmap: Bitmap): Bitmap {
                val width = bitmap.width
                val height = bitmap.height
                if (width <= 400 && height <= 400) {
                    return bitmap
                }
                val aspectRatio = width.toFloat() / height.toFloat()
                var newWidth = 400
                var newHeight = 400
                if (width > height) {
                    newHeight = Math.round(400 / aspectRatio)
                } else if (height > width) {
                    newWidth = Math.round(400 * aspectRatio)
                }
                return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
            }
        }
    }


}