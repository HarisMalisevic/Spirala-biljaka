package etf.rma.spirale.trefleAPI

import etf.rma.spirale.values.Constraints
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constraints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val trefleAPI: TrefleAPI by lazy {
        retrofit.create(TrefleAPI::class.java)
    }

}