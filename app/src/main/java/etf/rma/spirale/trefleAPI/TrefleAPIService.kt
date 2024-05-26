package etf.rma.spirale.trefleAPI

import etf.rma.spirale.biljka.Biljka
import retrofit2.http.GET
import retrofit2.http.Query

interface TrefleAPIService {
    @GET("api/v1/plants/{slug}")
    fun getBiljkaPoLatinskomNazivu(
        @Query("token") apiToken : String,
        @Query("slug") slug: String
    ) : List<Biljka>

}