package etf.rma.spirale.trefleAPI

import etf.rma.spirale.values.Constraints
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TrefleAPI {

    //TODO: Prvo pozvati query po scientific_name i odrediti ID
    //      onda odraditi pretragu po ID za species
    @GET("api/v1/species")
    suspend fun filterByScientificName(
        @Query("filter[scientific_name]") scientificName: String,
        @Query("token") apiToken: String = Constraints.API_TOKEN
    ): Response<TrefleSearchResponse>


    //TODO: Anotirati suspend fun getImage(biljka: Biljka): Bitmap {}

    //TODO: Anotirati suspend fun fixData(biljka: Biljka): Biljka {}

    //TODO: Anotirati suspend fun getPlantsWithFlowerColor(flowerColor: String, substr: String): List<Biljka> {}

}