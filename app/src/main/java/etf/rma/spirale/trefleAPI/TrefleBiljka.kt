package etf.rma.spirale.trefleAPI


import com.google.gson.annotations.SerializedName

// TODO: Klasu prilagoditi za rad sa species pretragom!
data class TrefleBiljka(
    @SerializedName("data") val `data`: Data
) {
    data class Data(
        @SerializedName("main_species") val mainSpecies: MainSpecies
    ) {
        data class MainSpecies(
            @SerializedName("common_name") val commonName: String,
            @SerializedName("edible") val edible: Boolean,
            @SerializedName("edible_part") val ediblePart: Any?,
            @SerializedName("family") val family: String,
            @SerializedName("family_common_name") val familyCommonName: Any?,
            @SerializedName("flower") val flower: Flower,
            @SerializedName("genus") val genus: String,
            @SerializedName("growth") val growth: Growth,
            @SerializedName("id") val id: Int,
            @SerializedName("image_url") val imageUrl: String,
            @SerializedName("scientific_name") val scientificName: String,
            @SerializedName("slug") val slug: String
        ) {
            data class Flower(
                @SerializedName("color") val color: Any?
            )

            data class Growth(
                @SerializedName("atmospheric_humidity") val atmosphericHumidity: Int,
                @SerializedName("light") val light: Int,
                @SerializedName("soil_texture") val soilTexture: Int
            )
        }
    }
}