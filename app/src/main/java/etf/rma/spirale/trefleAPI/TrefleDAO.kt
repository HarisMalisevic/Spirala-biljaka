package etf.rma.spirale.trefleAPI

import etf.rma.spirale.biljka.Biljka
import etf.rma.spirale.values.Constraints

object TrefleDAO {

    suspend fun getBiljkaPoLatinskomNazivu(latinskiNaziv: String): Biljka {
        val formatiranLatinskiNaziv = getFormatiranLatinskiNaziv(latinskiNaziv)
        return TrefleRetrofitClient.trefleAPI.getBiljkaPoLatinskomNazivu(Constraints.API_TOKEN, formatiranLatinskiNaziv)
    }

    private fun getFormatiranLatinskiNaziv(latinskiNaziv: String): String { // slug : https://docs.trefle.io/reference/#tag/Plants/operation/getPlant
        return latinskiNaziv.replace(" ", "-")
    }

    //TODO: Implementirati suspend fun getImage(biljka: Biljka): Bitmap {}

    //TODO: Implementirati suspend fun fixData(biljka: Biljka): Biljka {}

    //TODO: Implementirati suspend fun getPlantsWithFlowerColor(flowerColor: String, substr: String): List<Biljka> {}
}