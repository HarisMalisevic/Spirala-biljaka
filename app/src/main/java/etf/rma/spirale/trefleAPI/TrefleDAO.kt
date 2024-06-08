package etf.rma.spirale.trefleAPI

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import etf.rma.spirale.biljka.Biljka
import etf.rma.spirale.biljka.KlimatskiTip
import etf.rma.spirale.biljka.Zemljiste
import etf.rma.spirale.values.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.URL


class TrefleDAO {

    private var context: Context? = null
    private var defaultBitmap: Bitmap? = null

    constructor() {
    }

    constructor(context: Context) {
        this.context = context
        this.defaultBitmap = Constants.getDefaultBitmap(context)
    }


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
                //Log.d("getImage", "Trefle response is null!")
                return@withContext defaultBitmap!!
            }

            if (trefleSpeciesResponse.data.imageUrl == null) {
                //Log.d("getImage", "Image URL is null!")
                return@withContext defaultBitmap!!
            }

            val url = URL(trefleSpeciesResponse.data.imageUrl)

            //Log.d("getImage", url.toString())

            return@withContext try {
                BitmapFactory.decodeStream(url.openConnection().getInputStream())
            } catch (e: IOException) {
                println(e)
                defaultBitmap!!
            }
        }


    }

    suspend fun fixData(biljka: Biljka): Biljka {
        val latinskiNaziv = biljka.getLatinskiNaziv()
        val trefleSpeciesResponse = getBiljkaPoLatinskomNazivu(latinskiNaziv) ?: return biljka

        val biljkaBuilder = Biljka.Builder()

        biljkaBuilder.setNaziv(biljka.naziv)
        biljkaBuilder.setMedicinskoUpozorenje(biljka.medicinskoUpozorenje)

        for (mk in biljka.medicinskeKoristi) {
            biljkaBuilder.addMedicinskaKorist(mk)
        }

        for (jelo in biljka.jela) {
            biljkaBuilder.addJelo(jelo)
        }

        biljkaBuilder.setProfilOkusa(biljka.profilOkusa)

        fixPorodica(biljka, biljkaBuilder, trefleSpeciesResponse)
        fixEdible(biljka, biljkaBuilder, trefleSpeciesResponse)
        fixMedicinskoUpozorenje(biljka, biljkaBuilder, trefleSpeciesResponse)
        fixZemljiste(biljka, biljkaBuilder, trefleSpeciesResponse)
        fixKlima(biljka, biljkaBuilder, trefleSpeciesResponse)

        return biljkaBuilder.build()
    }

    private fun fixPorodica(
        biljka: Biljka,
        builder: Biljka.Builder, trefleSpeciesResponse: TrefleSpecies
    ) {
        val treflePorodica = trefleSpeciesResponse.data.family ?: return
        builder.setPorodica(treflePorodica)
        Log.d("Porodica", treflePorodica)
    }

    private fun fixEdible(
        biljka: Biljka,
        builder: Biljka.Builder, trefleSpeciesResponse: TrefleSpecies
    ) {
        val trefleEdible = trefleSpeciesResponse.data.edible ?: return

        if (!trefleEdible) {

            if (!biljka.medicinskoUpozorenje.contains("NIJE JESTIVO"))

                builder.addMedicinskoUpozorenje(" NIJE JESTIVO")

            // Ne dodaju se jela, Biljka Builder po defaultu ostavi prazno
        }
    }

    private fun fixMedicinskoUpozorenje(
        biljka: Biljka,
        builder: Biljka.Builder,
        trefleSpeciesResponse: TrefleSpecies
    ) {
        val trefleToxicity: String =
            (trefleSpeciesResponse.data.specifications.toxicity ?: return).toString()

        if (trefleToxicity == "none") return

        if (!biljka.medicinskoUpozorenje.contains("TOKSIČNO") || !biljka.medicinskoUpozorenje.contains(
                "TOKSICNO"
            )
        ) {
            builder.addMedicinskoUpozorenje(" TOKSIČNO")
        }
    }

    private fun fixZemljiste(
        biljka: Biljka,
        builder: Biljka.Builder,
        trefleSpeciesResponse: TrefleSpecies
    ) {
        val trefleSoilTexture = trefleSpeciesResponse.data.growth.soilTexture ?: return


        when (trefleSoilTexture) {
            9 -> builder.addZemljisniTip(Zemljiste.SLJUNOVITO)
            10 -> builder.addZemljisniTip(Zemljiste.KRECNJACKO)
            in 1..2 -> builder.addZemljisniTip(Zemljiste.GLINENO)
            in 3..4 -> builder.addZemljisniTip(Zemljiste.PJESKOVITO)
            in 5..6 -> builder.addZemljisniTip(Zemljiste.ILOVACA)
            in 7..8 -> builder.addZemljisniTip(Zemljiste.CRNICA)
        }


    }

    private fun fixKlima(
        biljka: Biljka,
        builder: Biljka.Builder,
        trefleSpeciesResponse: TrefleSpecies
    ) {
        val trefleLight = trefleSpeciesResponse.data.growth.light ?: return
        val trefleAtmosphericHumidity =
            trefleSpeciesResponse.data.growth.atmosphericHumidity ?: return

        if (trefleLight in 6..9 || trefleAtmosphericHumidity in 1..5)
            builder.addKlimatskiTip(KlimatskiTip.SREDOZEMNA)

        if (trefleLight in 8..10 || trefleAtmosphericHumidity in 7..10)
            builder.addKlimatskiTip(KlimatskiTip.TROPSKA)

        if (trefleLight in 6..9 || trefleAtmosphericHumidity in 5..8)
            builder.addKlimatskiTip(KlimatskiTip.SUBTROPSKA)

        if (trefleLight in 4..7 || trefleAtmosphericHumidity in 3..7)
            builder.addKlimatskiTip(KlimatskiTip.UMJERENA)

        if (trefleLight in 7..9 || trefleAtmosphericHumidity in 1..2)
            builder.addKlimatskiTip(KlimatskiTip.SUHA)

        if (trefleLight in 0..5 || trefleAtmosphericHumidity in 3..7)
            builder.addKlimatskiTip(KlimatskiTip.PLANINSKA)

    }

    //TODO: Implementirati suspend fun getPlantsWithFlowerColor(flowerColor: String, substr: String): List<Biljka> {}
}