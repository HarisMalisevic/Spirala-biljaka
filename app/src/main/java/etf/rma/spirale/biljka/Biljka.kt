package etf.rma.spirale.biljka

import java.io.Serializable

data class Biljka(
    val naziv: String,
    val porodica: String,
    val medicinskoUpozorenje: String,
    val medicinskeKoristi: List<MedicinskaKorist>,
    val profilOkusa: ProfilOkusaBiljke,
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
        private var narodniNaziv: String = ""
        private var latinskiNaziv: String = ""
        private var porodica: String = ""
        private var medicinskoUpozorenje: String = "Nema upozorenja"
        private var medicinskeKoristi: MutableList<MedicinskaKorist> = mutableListOf()
        private var profilOkusa: ProfilOkusaBiljke? = null
        private var jela: MutableList<String> = mutableListOf()
        private var klimatskiTipovi: MutableList<KlimatskiTip> = mutableListOf()
        private var zemljisniTipovi: MutableList<Zemljiste> = mutableListOf()

        fun setNarodniNaziv(narodniNaziv: String) = apply { this.narodniNaziv = narodniNaziv }
        fun setLatinskiNaziv(latinskiNaziv: String) =
            apply { this.latinskiNaziv = latinskiNaziv }

        fun setPorodica(porodica: String) = apply { this.porodica = porodica }
        fun setMedicinskoUpozorenje(medicinskoUpozorenje: String) =
            apply { this.medicinskoUpozorenje = medicinskoUpozorenje }

        fun addMedicinskaKorist(medicinskaKorist: MedicinskaKorist) =
            apply { this.medicinskeKoristi.add(medicinskaKorist) }

        fun setProfilOkusa(profilOkusa: ProfilOkusaBiljke) =
            apply { this.profilOkusa = profilOkusa }

        fun addJelo(jelo: String) = apply { this.jela.add(jelo) }
        fun addKlimatskiTip(klimatskiTip: KlimatskiTip) =
            apply { this.klimatskiTipovi.add(klimatskiTip) }

        fun addZemljisniTip(zemljisniTip: Zemljiste) =
            apply { this.zemljisniTipovi.add(zemljisniTip) }


        fun build(): Biljka {

            if (narodniNaziv.isEmpty()) throw IllegalArgumentException("Narodni naziv nije postavljen")
            if (latinskiNaziv.isEmpty()) throw IllegalArgumentException("Latinski naziv nije postavljen")
            if (porodica.isEmpty()) throw IllegalArgumentException("Porodica nije postavljena")
            if (profilOkusa == null) throw IllegalArgumentException("Profil okusa nije postavljen")
            if (klimatskiTipovi.isEmpty()) throw IllegalArgumentException("Mora biti bar jedan klimatski tip")
            if (zemljisniTipovi.isEmpty()) throw IllegalArgumentException("Mora biti bar jedan tip zemljista")
            if (medicinskeKoristi.isEmpty()) throw IllegalArgumentException("Mora biti bar jedna medicinska korist")
            if (jela.isEmpty()) throw IllegalArgumentException("Mora biti bar jedno jelo")

            val naziv = "$narodniNaziv ($latinskiNaziv)"

            return Biljka(
                naziv,
                porodica,
                medicinskoUpozorenje,
                medicinskeKoristi,
                profilOkusa!!,
                jela,
                klimatskiTipovi,
                zemljisniTipovi
            )
        }
    }
}
