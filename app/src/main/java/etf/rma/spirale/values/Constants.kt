package etf.rma.spirale.values

import android.graphics.BitmapFactory
import androidx.core.graphics.scale
import etf.rma.spirale.App
import etf.rma.spirale.R

class Constants {

    companion object {
        const val BASE_URL = "https://trefle.io/"
        const val API_TOKEN = "dQ-U3NITq16JJk5kThd_qavKBw03i9T8SH97R-ts3X8"
        val defaultBitmap = BitmapFactory.decodeResource(
            App.context.resources, R.drawable.plant
        ).scale(300, 300)
    }
}