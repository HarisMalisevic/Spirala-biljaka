package etf.rma.spirale.trefleAPI


import com.google.gson.annotations.SerializedName

data class TrefleSearchResponse(
    @SerializedName("data") val data: List<Data>,
    @SerializedName("links") val links: Links,
    @SerializedName("meta") val meta: Meta
) {
    data class Data(
        @SerializedName("author") val author: String,
        @SerializedName("bibliography") val bibliography: String,
        @SerializedName("common_name") val commonName: String?,
        @SerializedName("family") val family: String,
        @SerializedName("family_common_name") val familyCommonName: Any?,
        @SerializedName("genus") val genus: String,
        @SerializedName("genus_id") val genusId: Int,
        @SerializedName("id") val id: Int,
        @SerializedName("image_url") val imageUrl: String,
        @SerializedName("links") val links: Links,
        @SerializedName("rank") val rank: String,
        @SerializedName("scientific_name") val scientificName: String,
        @SerializedName("slug") val slug: String,
        @SerializedName("status") val status: String,
        @SerializedName("synonyms") val synonyms: List<String>,
        @SerializedName("year") val year: Int
    ) {
        data class Links(
            @SerializedName("genus") val genus: String,
            @SerializedName("plant") val plant: String,
            @SerializedName("self") val self: String
        )
    }

    data class Links(
        @SerializedName("first") val first: String,
        @SerializedName("last") val last: String,
        @SerializedName("self") val self: String
    )

    data class Meta(
        @SerializedName("total") val total: Int
    )
}