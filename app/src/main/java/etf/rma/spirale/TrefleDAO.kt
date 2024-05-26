package etf.rma.spirale

import etf.rma.spirale.Biljka.Biljka
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TrefleDAO {

    //TODO: Retrofit HTTP



    suspend fun getImage(biljka: Biljka) {
        val scientificName = biljka.getLatinskiNaziv()
    }

    //TODO: Implementirati suspend fun fixData(biljka: Biljka): Biljka {}

    //TODO: Implementirati suspend fun getPlantsWithFlowerColor(flowerColor: String, substr: String): List<Biljka> {}
}
