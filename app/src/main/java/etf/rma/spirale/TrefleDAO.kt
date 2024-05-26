package etf.rma.spirale

import android.graphics.Bitmap
import android.widget.Toast

class TrefleDAO {

    //TODO: Retrofit HTTP
    companion object {
        fun getImage(biljka: Biljka) {
            val scientificName = biljka.getLatinskiNaziv()
        }

        //TODO: Implementirati fun fixData(biljka: Biljka): Biljka {}

        //TODO: Implementirati fun getPlantsWithFlowerColor(flowerColor: String, substr: String): List<Biljka> {}
    }
}