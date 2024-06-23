package etf.rma.spirale.dataPersistence.database

import android.graphics.Bitmap
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
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            return stream.toByteArray()

        }
    }

}