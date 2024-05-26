package etf.rma.spirale.trefleAPI

import etf.rma.spirale.biljka.Biljka

object TrefleDAO{

    //TODO: Retrofit HTTP


    suspend fun getImage(biljka: Biljka) {
        val scientificName = biljka.getLatinskiNaziv()
    }

    //TODO: Implementirati suspend fun fixData(biljka: Biljka): Biljka {}

    //TODO: Implementirati suspend fun getPlantsWithFlowerColor(flowerColor: String, substr: String): List<Biljka> {}
}
