package etf.rma.spirale.trefleAPI

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import etf.rma.spirale.App
import etf.rma.spirale.R
import etf.rma.spirale.biljka.Biljka
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.URL


object TrefleDAO {

    private val defaultBitmap = BitmapFactory.decodeResource(
        App.context.resources,
        R.drawable.plant
    )

    // Private:
    private suspend fun getBiljkaPoLatinskomNazivu(latinskiNaziv: String): TrefleSearchResponse? {

        return withContext(Dispatchers.IO) {

            val trefleResponse = try {
                RetrofitClient.trefleAPI.filterByScientificName(latinskiNaziv)

            } catch (e: IOException) {
                Log.e("Exception!", "IOException - internet connection issue!")
                return@withContext null

            } catch (e: HttpException) {
                Log.e("Exception!", "HttpException")
                return@withContext null
            }

            if (!trefleResponse.isSuccessful || trefleResponse.body() == null) {
                Log.e("Error", "API Response not successful!")
                return@withContext null
            }

            val trefleSearchResponse = trefleResponse.body()

            Log.d("TrefleDAO", trefleResponse.code().toString())
            Log.d("TrefleDAO Image URL", trefleSearchResponse?.data?.get(0)?.imageUrl.toString())

            return@withContext trefleSearchResponse

        }
    }

    suspend fun getImage(biljka: Biljka): Bitmap {

        return withContext(Dispatchers.IO) {
            val latinskiNaziv = biljka.getLatinskiNaziv()
            val trefleSearchResponse = getBiljkaPoLatinskomNazivu(latinskiNaziv)

            if (trefleSearchResponse == null) {
                Log.d("getImage", "NULL!")
                return@withContext defaultBitmap
            }

            val url = URL(trefleSearchResponse.data[0].imageUrl)

            Log.d("getImage", url.toString())

            return@withContext try {
                BitmapFactory.decodeStream(url.openConnection().getInputStream())
            } catch (e: IOException) {
                println(e)
                defaultBitmap
            }
        }


    }

    //TODO: Implementirati suspend fun fixData(biljka: Biljka): Biljka {}

    //TODO: Implementirati suspend fun getPlantsWithFlowerColor(flowerColor: String, substr: String): List<Biljka> {}
}