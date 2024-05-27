package etf.rma.spirale.trefleAPI

import etf.rma.spirale.biljka.Biljka
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TrefleAPI {
    @GET("api/v1/plants/{slug}")
    suspend fun getBiljkaPoLatinskomNazivu(
        @Path("slug") formatiranLatinskiNaziv: String,
        @Query("token") apiToken : String
    ): Response<TrefleBiljka>


    //TODO: Anotirati suspend fun getImage(biljka: Biljka): Bitmap {}

    //TODO: Anotirati suspend fun fixData(biljka: Biljka): Biljka {}

    //TODO: Anotirati suspend fun getPlantsWithFlowerColor(flowerColor: String, substr: String): List<Biljka> {}

}