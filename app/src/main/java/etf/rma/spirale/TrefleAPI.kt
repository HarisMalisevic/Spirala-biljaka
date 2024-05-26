package etf.rma.spirale

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface TrefleAPI {

    companion object {

        private const val BASE_URL = "https://trefle.io/"

        private val trefleAPI = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TrefleAPI::class.java)
    }

    @GET("api/v1/plants/search")
    fun getBiljka(filter: String)

}