package etf.rma.spirale.trefleAPI

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.graphics.scale
import etf.rma.spirale.App
import etf.rma.spirale.R
import etf.rma.spirale.biljka.Biljka
import etf.rma.spirale.biljka.KlimatskiTip
import etf.rma.spirale.biljka.Zemljiste
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.URL


object TrefleDAO {

    private val defaultBitmap = BitmapFactory.decodeResource(
        App.context.resources, R.drawable.plant
    ).scale(300, 300)

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
                Log.d("getImage", "Trefle response is null!")
                return@withContext defaultBitmap
            }

            if (trefleSpeciesResponse.data.imageUrl == null) {
                Log.d("getImage", "Image URL is null!")
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
        fixZemljiste(biljka, trefleSpeciesResponse)






        return biljka
    }

    private fun fixPorodica(biljka: Biljka, trefleSpeciesResponse: TrefleSpecies) {
        val treflePorodica = trefleSpeciesResponse.data.family ?: return
        if (treflePorodica != "") biljka.porodica = treflePorodica
    }

    private fun fixEdible(biljka: Biljka, trefleSpeciesResponse: TrefleSpecies) {
        val trefleEdible = trefleSpeciesResponse.data.edible ?: return

        if (!trefleEdible) {

            if (!biljka.medicinskoUpozorenje.contains("NIJE JESTIVO")) biljka.medicinskoUpozorenje.plus(
                " NIJE JESTIVO"
            )

            biljka.jela.clear()
        }
    }

    private fun fixMedicinskoUpozorenje(biljka: Biljka, trefleSpeciesResponse: TrefleSpecies) {
        val trefleToxicity: String =
            (trefleSpeciesResponse.data.specifications.toxicity ?: return).toString()

        if (trefleToxicity == "none") return

        if (!biljka.medicinskoUpozorenje.contains("TOKSIČNO") || biljka.medicinskoUpozorenje.contains(
                "TOKSICNO"
            )
        ) {
            biljka.medicinskoUpozorenje.plus(" TOKSIČNO")
        }
    }

    private fun fixZemljiste(biljka: Biljka, trefleSpeciesResponse: TrefleSpecies) {
        val trefleSoilTexture = trefleSpeciesResponse.data.growth.soilTexture ?: return

        biljka.zemljisniTipovi.clear()

        when (trefleSoilTexture) {
            9 -> biljka.zemljisniTipovi.add(Zemljiste.SLJUNOVITO)
            10 -> biljka.zemljisniTipovi.add(Zemljiste.KRECNJACKO)
            in 1..2 -> biljka.zemljisniTipovi.add(Zemljiste.GLINENO)
            in 3..4 -> biljka.zemljisniTipovi.add(Zemljiste.PJESKOVITO)
            in 5..6 -> biljka.zemljisniTipovi.add(Zemljiste.ILOVACA)
            in 7..8 -> biljka.zemljisniTipovi.add(Zemljiste.CRNICA)
        }


    }

    private fun fixKlima(biljka: Biljka, trefleSpeciesResponse: TrefleSpecies) {
        val trefleLight = trefleSpeciesResponse.data.growth.light ?: return
        val trefleAtmosphericHumidity =
            trefleSpeciesResponse.data.growth.atmosphericHumidity ?: return

        biljka.klimatskiTipovi.clear()


        if (trefleLight in 6..9 && trefleAtmosphericHumidity in 1..5)
            biljka.klimatskiTipovi.add(KlimatskiTip.SREDOZEMNA)

        if (trefleLight in 8..10 && trefleAtmosphericHumidity in 7..10)
            biljka.klimatskiTipovi.add(KlimatskiTip.TROPSKA)

        if (trefleLight in 6..9 && trefleAtmosphericHumidity in 5..8)
            biljka.klimatskiTipovi.add(KlimatskiTip.SUBTROPSKA)

        if (trefleLight in 4..7 && trefleAtmosphericHumidity in 3..7)
            biljka.klimatskiTipovi.add(KlimatskiTip.UMJERENA)

        if (trefleLight in 7..9 && trefleAtmosphericHumidity in 1..2)
            biljka.klimatskiTipovi.add(KlimatskiTip.SUHA)

        if (trefleLight in 0..5 && trefleAtmosphericHumidity in 3..7)
            biljka.klimatskiTipovi.add(KlimatskiTip.PLANINSKA)


    }

    //TODO: Implementirati suspend fun getPlantsWithFlowerColor(flowerColor: String, substr: String): List<Biljka> {}
}