package etf.rma.spirale.dataPersistence.trefleAPI

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

        if (biljka.profilOkusa != null)
            biljkaBuilder.setProfilOkusa(biljka.profilOkusa)

        fixPorodica(biljka, biljkaBuilder, trefleSpeciesResponse)
        fixEdible(biljka, biljkaBuilder, trefleSpeciesResponse)
        fixMedicinskoUpozorenje(biljka, biljkaBuilder, trefleSpeciesResponse)
        fixZemljiste(biljka, biljkaBuilder, trefleSpeciesResponse)
        fixKlima(biljka, biljkaBuilder, trefleSpeciesResponse)
        val fixedBiljka = biljkaBuilder.build()
        fixedBiljka.onlineChecked = true
        return fixedBiljka
    }

    suspend fun getPlantsWithFlowerColor(flowerColor: String, substr: String): List<Biljka> {
        val trefleSearchResponse = try {
            RetrofitClient.trefleAPI.filterByFlowerColor(flowerColor, substr)
        } catch (e: IOException) {
            Log.e("Exception!", "IOException - internet connection issue!")
            return emptyList()
        } catch (e: HttpException) {
            Log.e("Exception!", "HttpException")
            return emptyList()
        }

        // Log.d("getPlantsWithFlowerColor", trefleSearchResponse.toString())

        if (!trefleSearchResponse.isSuccessful || trefleSearchResponse.body() == null) {
            Log.e("Error", "API Response not successful!")
            return emptyList()
        }

        val plants = trefleSearchResponse.body()!!.data.map { treflePlant ->
            Biljka(
                naziv = (treflePlant.commonName ?: "") + " (" + treflePlant.scientificName + ")",
                porodica = treflePlant.family,
                medicinskoUpozorenje = "",
                medicinskeKoristi = emptyList(),
                profilOkusa = null,
                jela = emptyList(),
                klimatskiTipovi = emptyList(),
                zemljisniTipovi = emptyList()
            )
        }

        return plants
    }


    private fun fixPorodica(
        biljka: Biljka,
        builder: Biljka.Builder, trefleSpeciesResponse: TrefleSpecies
    ) {
        val treflePorodica = trefleSpeciesResponse.data.family ?: return
        builder.setPorodica(treflePorodica)
        // Log.d("Porodica", treflePorodica)
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
        val trefleSoilTexture = (trefleSpeciesResponse.data.growth.soilTexture ?: return) as Int


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
        val trefleLight = (trefleSpeciesResponse.data.growth.light ?: return) as Double
        val trefleAtmosphericHumidity =
            (trefleSpeciesResponse.data.growth.atmosphericHumidity ?: return) as Double

        if (trefleLight in 6.0..9.0 || trefleAtmosphericHumidity in 1.0..5.0)
            builder.addKlimatskiTip(KlimatskiTip.SREDOZEMNA)

        if (trefleLight in 8.0..10.0 || trefleAtmosphericHumidity in 7.0..10.0)
            builder.addKlimatskiTip(KlimatskiTip.TROPSKA)

        if (trefleLight in 6.0..9.0 || trefleAtmosphericHumidity in 5.0..8.0)
            builder.addKlimatskiTip(KlimatskiTip.SUBTROPSKA)

        if (trefleLight in 4.0..7.0 || trefleAtmosphericHumidity in 3.0..7.0)
            builder.addKlimatskiTip(KlimatskiTip.UMJERENA)

        if (trefleLight in 7.0..9.0 || trefleAtmosphericHumidity in 1.0..2.0)
            builder.addKlimatskiTip(KlimatskiTip.SUHA)

        if (trefleLight in 0.0..5.0 || trefleAtmosphericHumidity in 3.0..7.0)
            builder.addKlimatskiTip(KlimatskiTip.PLANINSKA)

    }

}