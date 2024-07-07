package etf.rma.spirale.values

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.graphics.scale
import etf.rma.spirale.App
import etf.rma.spirale.R

object Constants {

    // Privremeno je HTTP, dok API ne vrati certifikat
    const val BASE_URL = "http://trefle.io/"
    const val API_TOKEN = "dQ-U3NITq16JJk5kThd_qavKBw03i9T8SH97R-ts3X8"

    fun getDefaultBitmap(context: Context): Bitmap {
        return BitmapFactory.decodeResource(
            context.resources, R.drawable.plant_1080p
        ).scale(300, 300)
    }

}
