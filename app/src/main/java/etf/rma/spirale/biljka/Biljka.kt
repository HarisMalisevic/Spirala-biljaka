package etf.rma.spirale.biljka

import android.util.Log
import java.io.Serializable

data class Biljka(
    val naziv: String,
    val porodica: String,
    val medicinskoUpozorenje: String,
    val medicinskeKoristi: List<MedicinskaKorist>,
    val profilOkusa: ProfilOkusaBiljke?,
    val jela: List<String>,
    val klimatskiTipovi: List<KlimatskiTip>,
    val zemljisniTipovi: List<Zemljiste>
) : Serializable {

    fun getLatinskiNaziv(): String {
        val regex = "\\(([^)]+)\\)".toRegex()
        val matchResult = regex.find(this.naziv)
        return matchResult!!.groups[1]!!.value
    }

    class Builder {
        private var naziv: String = ""
        private var porodica: String = ""
        private var medicinskoUpozorenje: String = "Nema upozorenja"
        private var medicinskeKoristi: MutableList<MedicinskaKorist> = mutableListOf()
        private var profilOkusa: ProfilOkusaBiljke? = null
        private var jela: MutableList<String> = mutableListOf()
        private var klimatskiTipovi: MutableList<KlimatskiTip> = mutableListOf()
        private var zemljisniTipovi: MutableList<Zemljiste> = mutableListOf()

        fun setNaziv(naziv: String) {
            this.naziv = naziv
        }

        fun setPorodica(porodica: String) {
            this.porodica = porodica
        }

        fun setMedicinskoUpozorenje(medicinskoUpozorenje: String) {
            this.medicinskoUpozorenje = medicinskoUpozorenje
        }

        fun addMedicinskoUpozorenje(medicinskoUpozorenje: String) {
            this.medicinskoUpozorenje = this.medicinskoUpozorenje.plus(medicinskoUpozorenje)
        }

        fun addMedicinskaKorist(medicinskaKorist: MedicinskaKorist) {
            this.medicinskeKoristi.add(medicinskaKorist)
        }

        fun setProfilOkusa(profilOkusa: ProfilOkusaBiljke) {
            this.profilOkusa = profilOkusa
        }

        fun addJelo(jelo: String) {
            this.jela.add(jelo)
        }

        fun addKlimatskiTip(klimatskiTip: KlimatskiTip) {
            this.klimatskiTipovi.add(klimatskiTip)
        }

        fun addZemljisniTip(zemljisniTip: Zemljiste) {
            this.zemljisniTipovi.add(zemljisniTip)
        }

        fun build(): Biljka {

            if (naziv.isEmpty()) throw IllegalArgumentException("Naziv nije postavljen")
            if (porodica.isEmpty()) throw IllegalArgumentException("Porodica nije postavljena")
            // if (profilOkusa == null) throw IllegalArgumentException("Profil okusa nije postavljen")
            // if (klimatskiTipovi.isEmpty()) throw IllegalArgumentException("Mora biti bar jedan klimatski tip")
            // if (zemljisniTipovi.isEmpty()) throw IllegalArgumentException("Mora biti bar jedan tip zemljista")
            // if (medicinskeKoristi.isEmpty()) throw IllegalArgumentException("Mora biti bar jedna medicinska korist")
            // if (jela.isEmpty()) throw IllegalArgumentException("Mora biti bar jedno jelo")

            // Log.d("BUILDER Porodica", porodica)
            return Biljka(
                naziv,
                porodica,
                medicinskoUpozorenje,
                medicinskeKoristi,
                profilOkusa,
                jela,
                klimatskiTipovi,
                zemljisniTipovi
            )
        }
    }
}
