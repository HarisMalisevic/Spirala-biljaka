package etf.rma.spirale.trefleAPI

import android.util.Log
import etf.rma.spirale.biljka.Biljka
import etf.rma.spirale.values.Constraints
import java.util.Locale

object TrefleDAO {

    suspend fun getBiljkaPoLatinskomNazivu(latinskiNaziv: String) {
        val formatiranLatinskiNaziv = getFormatiranLatinskiNaziv(latinskiNaziv)


        val trefleBiljka = RetrofitClient.trefleAPI.getBiljkaPoLatinskomNazivu(
            Constraints.API_TOKEN,
            formatiranLatinskiNaziv
        )

        Log.d("EY OUKEJ!", trefleBiljka.data.mainSpecies.commonName)

    }

    private fun getFormatiranLatinskiNaziv(latinskiNaziv: String): String { // slug : https://docs.trefle.io/reference/#tag/Plants/operation/getPlant
        latinskiNaziv.replace(" ", "-")
        return latinskiNaziv.lowercase()
    }

    //TODO: Implementirati suspend fun getImage(biljka: Biljka): Bitmap {}

    //TODO: Implementirati suspend fun fixData(biljka: Biljka): Biljka {}

    //TODO: Implementirati suspend fun getPlantsWithFlowerColor(flowerColor: String, substr: String): List<Biljka> {}
}