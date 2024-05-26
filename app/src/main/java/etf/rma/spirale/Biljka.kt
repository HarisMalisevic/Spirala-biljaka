package etf.rma.spirale

import java.io.Serializable
data class Biljka (
    val naziv: String,
    val porodica: String,
    val medicinskoUpozorenje: String,
    val medicinskeKoristi: List<MedicinskaKorist>,
    val profilOkusa: ProfilOkusaBiljke,
    val jela: List<String>,
    val klimatskiTipovi: List<KlimatskiTip>,
    val zemljisniTipovi: List<Zemljiste>
) : Serializable{

    fun getLatinskiNaziv(): String? {
        val regex = "\\(([^)]+)\\)".toRegex()
        val matchResult = regex.find(this.naziv)
        return matchResult?.groups?.get(1)?.value
    }

}
