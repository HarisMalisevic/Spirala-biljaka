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
        App.context.resources, R.drawable.plant
    )

    // Private:
    private suspend fun getBiljkaPoLatinskomNazivu(latinskiNaziv: String): TrefleSpecies? {

        return withContext(Dispatchers.IO) {

            val trefleSearchResponse = try {
                RetrofitClient.trefleAPI.filterByScientificName(latinskiNaziv)

            } catch (e: IOException) {
                Log.e("Exception!", "IOException - internet connection issue!")
                return@withContext null

            } catch (e: HttpException) {
                Log.e("Exception!", "HttpException")
                return@withContext null
            }

            if (!trefleSearchResponse.isSuccessful || trefleSearchResponse.body() == null) {
                Log.e("Error", "API Response not successful!")
                return@withContext null
            }

            val firstSpeciesID = trefleSearchResponse.body()!!.data[0].id

            val trefleSpeciesResponse = try {
                RetrofitClient.trefleAPI.getSpeciesByID(firstSpeciesID)
            } catch (e: IOException) {
                Log.e("Exception!", "IOException - internet connection issue!")
                return@withContext null

            } catch (e: HttpException) {
                Log.e("Exception!", "HttpException")
                return@withContext null
            }

            if (!trefleSpeciesResponse.isSuccessful || trefleSpeciesResponse.body() == null) {
                Log.e("Error", "API Response not successful!")
                return@withContext null
            }

            return@withContext trefleSpeciesResponse.body()

        }
    }

    suspend fun getImage(biljka: Biljka): Bitmap {

        val latinskiNaziv = biljka.getLatinskiNaziv()
        val trefleSpeciesResponse = getBiljkaPoLatinskomNazivu(latinskiNaziv)

        return withContext(Dispatchers.IO) {

            if (trefleSpeciesResponse == null) {
                Log.d("getImage", "NULL!")
                return@withContext defaultBitmap
            }

            val url = URL(trefleSpeciesResponse.data.imageUrl)

            Log.d("getImage", url.toString())

            return@withContext try {
                BitmapFactory.decodeStream(url.openConnection().getInputStream())
            } catch (e: IOException) {
                println(e)
                defaultBitmap
            }
        }


    }

    suspend fun fixData(biljka: Biljka): Biljka {
        val latinskiNaziv = biljka.getLatinskiNaziv()
        val trefleSpeciesResponse = getBiljkaPoLatinskomNazivu(latinskiNaziv) ?: return biljka

        fixPorodica(biljka, trefleSpeciesResponse)

        fixEdible(biljka, trefleSpeciesResponse)

        fixMedicinskoUpozorenje(biljka, trefleSpeciesResponse)







        return biljka
    }

    private fun fixPorodica(biljka: Biljka, trefleSpeciesResponse: TrefleSpecies) {
        val treflePorodica = trefleSpeciesResponse.data.family
        if (treflePorodica != "") biljka.porodica = treflePorodica
    }

    private fun fixEdible(biljka: Biljka, trefleSpeciesResponse: TrefleSpecies) {
        val trefleEdible = trefleSpeciesResponse.data.edible

        if (!trefleEdible) {

            if (!biljka.medicinskoUpozorenje.contains("NIJE JESTIVO"))
                biljka.medicinskoUpozorenje.plus(" NIJE JESTIVO")

            biljka.jela.clear()
        }
    }

    private fun fixMedicinskoUpozorenje(biljka: Biljka, trefleSpeciesResponse: TrefleSpecies) {
        val trefleToxicity = trefleSpeciesResponse.data.specifications.toxicity

        if (trefleToxicity != null) {

            if (!biljka.medicinskoUpozorenje.contains("TOKSIČNO")
                || biljka.medicinskoUpozorenje.contains("TOKSICNO")
            ) {
                biljka.medicinskoUpozorenje.plus(" TOKSIČNO")
            }
        }
    }




    //TODO: Implementirati suspend fun getPlantsWithFlowerColor(flowerColor: String, substr: String): List<Biljka> {}
}