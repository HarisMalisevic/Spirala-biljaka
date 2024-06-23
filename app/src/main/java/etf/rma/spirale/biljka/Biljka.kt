package etf.rma.spirale.biljka

import androidx.room.ColumnInfo
import java.io.Serializable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "biljka")
data class Biljka(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "naziv") val naziv: String,
    @ColumnInfo(name = "porodica") val porodica: String,
    @ColumnInfo(name = "medicinskoUpozorenje") val medicinskoUpozorenje: String,
    @ColumnInfo(name = "medicinskeKoristi") val medicinskeKoristi: List<MedicinskaKorist>,
    @ColumnInfo(name = "profilOkusa") val profilOkusa: ProfilOkusaBiljke?,
    @ColumnInfo(name = "jela") val jela: List<String>,
    @ColumnInfo(name = "klimatskiTipovi") val klimatskiTipovi: List<KlimatskiTip>,
    @ColumnInfo(name = "zemljisniTipovi") val zemljisniTipovi: List<Zemljiste>,
    @ColumnInfo(name = "onlineChecked") var onlineChecked: Boolean = false
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
                naziv = naziv,
                porodica = porodica,
                medicinskoUpozorenje = medicinskoUpozorenje,
                medicinskeKoristi = medicinskeKoristi,
                profilOkusa = profilOkusa,
                jela = jela,
                klimatskiTipovi = klimatskiTipovi,
                zemljisniTipovi = zemljisniTipovi
            )
        }
    }
}
