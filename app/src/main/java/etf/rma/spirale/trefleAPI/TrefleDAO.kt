package etf.rma.spirale.trefleAPI

import android.util.Log
import etf.rma.spirale.values.Constraints
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object TrefleDAO {

    suspend fun getBiljkaPoLatinskomNazivu(latinskiNaziv: String) {
        //TODO: treba vratiti Biljka, void je privremeno

        return withContext(Dispatchers.IO) {

            // TODO: Handle za Success i Fail
            val formatiranLatinskiNaziv = getFormatiranLatinskiNaziv(latinskiNaziv)
            val trefleResponse = RetrofitClient.trefleAPI.getBiljkaPoLatinskomNazivu(
                formatiranLatinskiNaziv, Constraints.API_TOKEN
            )

            Log.d("TrefleDAO", trefleResponse.code().toString())
            Log.d(
                "TrefleDAO Image URL", trefleResponse.body()?.data?.mainSpecies?.imageUrl.toString()
            )
        }
    }

    private fun getFormatiranLatinskiNaziv(latinskiNaziv: String): String {
        // slug : https://docs.trefle.io/reference/#tag/Plants/operation/getPlant
        return latinskiNaziv.replace(" ", "-").lowercase()
    }

    //TODO: Implementirati suspend fun getImage(biljka: Biljka): Bitmap {}

    //TODO: Implementirati suspend fun fixData(biljka: Biljka): Biljka {}

    //TODO: Implementirati suspend fun getPlantsWithFlowerColor(flowerColor: String, substr: String): List<Biljka> {}
}