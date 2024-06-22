package etf.rma.spirale.dataPersistence.trefleAPI


import com.google.gson.annotations.SerializedName

data class TrefleSpecies(
    @SerializedName("data") val data: Data, @SerializedName("meta") val meta: Meta
) {
    data class Data(
        @SerializedName("author") val author: String,
        @SerializedName("bibliography") val bibliography: String,
        @SerializedName("common_name") val commonName: String,
        @SerializedName("common_names") val commonNames: CommonNames,
        @SerializedName("distribution") val distribution: Distribution,
        @SerializedName("distributions") val distributions: Distributions,
        @SerializedName("duration") val duration: Any?,
        @SerializedName("edible") val edible: Boolean?,
        @SerializedName("edible_part") val ediblePart: Any?,
        @SerializedName("family") val family: String?,
        @SerializedName("family_common_name") val familyCommonName: String?,
        @SerializedName("flower") val flower: Flower,
        @SerializedName("foliage") val foliage: Foliage,
        @SerializedName("fruit_or_seed") val fruitOrSeed: FruitOrSeed,
        @SerializedName("genus") val genus: String,
        @SerializedName("genus_id") val genusId: Int,
        @SerializedName("growth") val growth: Growth,
        @SerializedName("id") val id: Int,
        @SerializedName("image_url") val imageUrl: String?,
        @SerializedName("images") val images: Images,
        @SerializedName("links") val links: Links,
        @SerializedName("observations") val observations: String,
        @SerializedName("rank") val rank: String,
        @SerializedName("scientific_name") val scientificName: String,
        @SerializedName("slug") val slug: String,
        @SerializedName("sources") val sources: List<Source>,
        @SerializedName("specifications") val specifications: Specifications,
        @SerializedName("status") val status: String,
        @SerializedName("synonyms") val synonyms: List<Any>,
        @SerializedName("vegetable") val vegetable: Boolean,
        @SerializedName("year") val year: Int
    ) {
        data class CommonNames(
            @SerializedName("af") val af: List<String>,
            @SerializedName("ar") val ar: List<String>,
            @SerializedName("az") val az: List<String>,
            @SerializedName("be") val be: List<String>,
            @SerializedName("br") val br: List<String>,
            @SerializedName("ca") val ca: List<String>,
            @SerializedName("ces") val ces: List<String>,
            @SerializedName("cs") val cs: List<String>,
            @SerializedName("da") val da: List<String>,
            @SerializedName("dan") val dan: List<String>,
            @SerializedName("de") val de: List<String>,
            @SerializedName("deu") val deu: List<String>,
            @SerializedName("el") val el: List<String>,
            @SerializedName("ell") val ell: List<String>,
            @SerializedName("en") val en: List<String>,
            @SerializedName("eng") val eng: List<String>,
            @SerializedName("eo") val eo: List<String>,
            @SerializedName("es") val es: List<String>,
            @SerializedName("eu") val eu: List<String>,
            @SerializedName("fa") val fa: List<String>,
            @SerializedName("fi") val fi: List<String>,
            @SerializedName("fr") val fr: List<String>,
            @SerializedName("fra") val fra: List<String>,
            @SerializedName("ga") val ga: List<String>,
            @SerializedName("gl") val gl: List<String>,
            @SerializedName("he") val he: List<String>,
            @SerializedName("hu") val hu: List<String>,
            @SerializedName("hun") val hun: List<String>,
            @SerializedName("it") val it: List<String>,
            @SerializedName("kn") val kn: List<String>,
            @SerializedName("ko") val ko: List<String>,
            @SerializedName("lt") val lt: List<String>,
            @SerializedName("ml") val ml: List<String>,
            @SerializedName("nb") val nb: List<String>,
            @SerializedName("nl") val nl: List<String>,
            @SerializedName("nld") val nld: List<String>,
            @SerializedName("nn") val nn: List<String>,
            @SerializedName("nno") val nno: List<String>,
            @SerializedName("nob") val nob: List<String>,
            @SerializedName("pl") val pl: List<String>,
            @SerializedName("por") val por: List<String>,
            @SerializedName("pt") val pt: List<String>,
            @SerializedName("ru") val ru: List<String>,
            @SerializedName("sk") val sk: List<String>,
            @SerializedName("sr") val sr: List<String>,
            @SerializedName("sv") val sv: List<String>,
            @SerializedName("swe") val swe: List<String>,
            @SerializedName("th") val th: List<String>,
            @SerializedName("tr") val tr: List<String>,
            @SerializedName("tur") val tur: List<String>,
            @SerializedName("uk") val uk: List<String>,
            @SerializedName("ur") val ur: List<String>,
            @SerializedName("vi") val vi: List<String>,
            @SerializedName("zh") val zh: List<String>,
            @SerializedName("zh-tw") val zhTw: List<String>
        )

        data class Distribution(
            @SerializedName("introduced") val introduced: List<String>,
            @SerializedName("native") val native: List<String>
        )

        data class Distributions(
            @SerializedName("introduced") val introduced: List<Introduced>,
            @SerializedName("native") val native: List<Native>
        ) {
            data class Introduced(
                @SerializedName("id") val id: Int,
                @SerializedName("links") val links: Links,
                @SerializedName("name") val name: String,
                @SerializedName("slug") val slug: String,
                @SerializedName("species_count") val speciesCount: Int,
                @SerializedName("tdwg_code") val tdwgCode: String,
                @SerializedName("tdwg_level") val tdwgLevel: Int
            ) {
                data class Links(
                    @SerializedName("plants") val plants: String,
                    @SerializedName("self") val self: String,
                    @SerializedName("species") val species: String
                )
            }

            data class Native(
                @SerializedName("id") val id: Int,
                @SerializedName("links") val links: Links,
                @SerializedName("name") val name: String,
                @SerializedName("slug") val slug: String,
                @SerializedName("species_count") val speciesCount: Int,
                @SerializedName("tdwg_code") val tdwgCode: String,
                @SerializedName("tdwg_level") val tdwgLevel: Int
            ) {
                data class Links(
                    @SerializedName("plants") val plants: String,
                    @SerializedName("self") val self: String,
                    @SerializedName("species") val species: String
                )
            }
        }

        data class Flower(
            @SerializedName("color") val color: Any?,
            @SerializedName("conspicuous") val conspicuous: Any?
        )

        data class Foliage(
            @SerializedName("color") val color: Any?,
            @SerializedName("leaf_retention") val leafRetention: Any?,
            @SerializedName("texture") val texture: Any?
        )

        data class FruitOrSeed(
            @SerializedName("color") val color: Any?,
            @SerializedName("conspicuous") val conspicuous: Any?,
            @SerializedName("seed_persistence") val seedPersistence: Any?,
            @SerializedName("shape") val shape: Any?
        )

        data class Growth(
            @SerializedName("atmospheric_humidity") val atmosphericHumidity: Any?,
            @SerializedName("bloom_months") val bloomMonths: Any?,
            @SerializedName("days_to_harvest") val daysToHarvest: Any?,
            @SerializedName("description") val description: Any?,
            @SerializedName("fruit_months") val fruitMonths: Any?,
            @SerializedName("growth_months") val growthMonths: Any?,
            @SerializedName("light") val light: Any?,
            @SerializedName("maximum_precipitation") val maximumPrecipitation: MaximumPrecipitation,
            @SerializedName("maximum_temperature") val maximumTemperature: MaximumTemperature,
            @SerializedName("minimum_precipitation") val minimumPrecipitation: MinimumPrecipitation,
            @SerializedName("minimum_root_depth") val minimumRootDepth: MinimumRootDepth,
            @SerializedName("minimum_temperature") val minimumTemperature: MinimumTemperature,
            @SerializedName("ph_maximum") val phMaximum: Any?,
            @SerializedName("ph_minimum") val phMinimum: Any?,
            @SerializedName("row_spacing") val rowSpacing: RowSpacing,
            @SerializedName("soil_humidity") val soilHumidity: Any?,
            @SerializedName("soil_nutriments") val soilNutriments: Any?,
            @SerializedName("soil_salinity") val soilSalinity: Any?,
            @SerializedName("soil_texture") val soilTexture: Any?,
            @SerializedName("sowing") val sowing: Any?,
            @SerializedName("spread") val spread: Spread
        ) {
            data class MaximumPrecipitation(
                @SerializedName("mm") val mm: Any?
            )

            data class MaximumTemperature(
                @SerializedName("deg_c") val degC: Any?, @SerializedName("deg_f") val degF: Any?
            )

            data class MinimumPrecipitation(
                @SerializedName("mm") val mm: Any?
            )

            data class MinimumRootDepth(
                @SerializedName("cm") val cm: Any?
            )

            data class MinimumTemperature(
                @SerializedName("deg_c") val degC: Any?, @SerializedName("deg_f") val degF: Any?
            )

            data class RowSpacing(
                @SerializedName("cm") val cm: Any?
            )

            data class Spread(
                @SerializedName("cm") val cm: Any?
            )
        }

        data class Images(
            @SerializedName("bark") val bark: List<Bark>,
            @SerializedName("flower") val flower: List<Flower>,
            @SerializedName("fruit") val fruit: List<Fruit>,
            @SerializedName("habit") val habit: List<Habit>,
            @SerializedName("leaf") val leaf: List<Leaf>,
            @SerializedName("other") val other: List<Other>,
            @SerializedName("") val x: List<X>
        ) {
            data class Bark(
                @SerializedName("copyright") val copyright: String,
                @SerializedName("id") val id: Int,
                @SerializedName("image_url") val imageUrl: String
            )

            data class Flower(
                @SerializedName("copyright") val copyright: String,
                @SerializedName("id") val id: Int,
                @SerializedName("image_url") val imageUrl: String
            )

            data class Fruit(
                @SerializedName("copyright") val copyright: String,
                @SerializedName("id") val id: Int,
                @SerializedName("image_url") val imageUrl: String
            )

            data class Habit(
                @SerializedName("copyright") val copyright: String,
                @SerializedName("id") val id: Int,
                @SerializedName("image_url") val imageUrl: String
            )

            data class Leaf(
                @SerializedName("copyright") val copyright: String,
                @SerializedName("id") val id: Int,
                @SerializedName("image_url") val imageUrl: String
            )

            data class Other(
                @SerializedName("copyright") val copyright: String,
                @SerializedName("id") val id: Int,
                @SerializedName("image_url") val imageUrl: String
            )

            data class X(
                @SerializedName("copyright") val copyright: String,
                @SerializedName("id") val id: Int,
                @SerializedName("image_url") val imageUrl: String
            )
        }

        data class Links(
            @SerializedName("genus") val genus: String,
            @SerializedName("plant") val plant: String,
            @SerializedName("self") val self: String
        )

        data class Source(
            @SerializedName("citation") val citation: String?,
            @SerializedName("id") val id: String,
            @SerializedName("last_update") val lastUpdate: String,
            @SerializedName("name") val name: String,
            @SerializedName("url") val url: String?
        )

        data class Specifications(
            @SerializedName("average_height") val averageHeight: AverageHeight,
            @SerializedName("growth_form") val growthForm: Any?,
            @SerializedName("growth_habit") val growthHabit: Any?,
            @SerializedName("growth_rate") val growthRate: Any?,
            @SerializedName("ligneous_type") val ligneousType: Any?,
            @SerializedName("maximum_height") val maximumHeight: MaximumHeight,
            @SerializedName("nitrogen_fixation") val nitrogenFixation: Any?,
            @SerializedName("shape_and_orientation") val shapeAndOrientation: Any?,
            @SerializedName("toxicity") val toxicity: Any?
        ) {
            data class AverageHeight(
                @SerializedName("cm") val cm: Any?
            )

            data class MaximumHeight(
                @SerializedName("cm") val cm: Any?
            )
        }
    }

    data class Meta(
        @SerializedName("images_count") val imagesCount: Int,
        @SerializedName("last_modified") val lastModified: String,
        @SerializedName("sources_count") val sourcesCount: Int,
        @SerializedName("synonyms_count") val synonymsCount: Int
    )
}