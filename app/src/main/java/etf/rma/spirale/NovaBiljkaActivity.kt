package etf.rma.spirale

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast

class NovaBiljkaActivity : AppCompatActivity() {

    private lateinit var nazivET: EditText
    private lateinit var porodicaET: EditText
    private lateinit var medicinskoUpozorenjeET: EditText
    private lateinit var jeloET: EditText

    private lateinit var medicinskaKoristLV: ListView
    private lateinit var klimatskiTipLV: ListView
    private lateinit var zemljisniTipLV: ListView

    private lateinit var profilOkusaLV: ListView
    private lateinit var jelaLV: ListView

    private lateinit var dodajJeloBtn: Button
    private lateinit var dodajBiljkuBtn: Button
    private lateinit var uslikajBiljkuBtn: Button

    private lateinit var slikaIV: ImageView


    private var jelaNoveBiljke: MutableList<String> = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nova_biljka)

        //TODO: EditText: nazivET, porodicaET, medicinskoUpozorenjeET, jeloET
        //TODO: ListView: medicinskaKoristLV, klimatskiTipLV, zemljisniTipLV
        //                profilOkusaLV, jelaLV
        //TODO: Button: dodajJeloBtn, dodajBiljkuBtn, uslikajBiljkuBtn
        //TODO: ImageView: slikaIV

        //DONE: Popuniti medicinskaKoristLV, klimatskiTipLV i zemljisniTipLV
        //DONE: Dodavanje novog jela u jelaLV
        //TODO: Slikanje biljke

        //TODO: Validacija (setError)
        //TODO: Dodavanje u MainActivity Listu
        //TODO: TESTIRANJE


        setupJeloET()
        setupJelaLV()

        setupMedicinskaKoristLV()
        setupKlimatskiTipLV()
        setupZemljisniTipLV()

        setupDodajJeloBtn()
    }

    private fun setupMedicinskaKoristLV() {
        medicinskaKoristLV = findViewById(R.id.medicinskaKoristLV)

        val medicinskeKoristi: Array<MedicinskaKorist> = MedicinskaKorist.entries.toTypedArray()

        val arrayAdapter: ArrayAdapter<MedicinskaKorist> = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            medicinskeKoristi
        )
        medicinskaKoristLV.adapter = arrayAdapter
        medicinskaKoristLV.choiceMode = ListView.CHOICE_MODE_MULTIPLE

    }

    private fun setupKlimatskiTipLV() {
        klimatskiTipLV = findViewById(R.id.klimatskiTipLV)

        val klimatskiTipovi: Array<KlimatskiTip> = KlimatskiTip.entries.toTypedArray()

        val arrayAdapter: ArrayAdapter<KlimatskiTip> = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            klimatskiTipovi
        )
        klimatskiTipLV.adapter = arrayAdapter
        klimatskiTipLV.choiceMode = ListView.CHOICE_MODE_MULTIPLE

    }

    private fun setupZemljisniTipLV() {
        zemljisniTipLV = findViewById(R.id.zemljisniTipLV)

        val zemljisniTipovi: Array<Zemljiste> = Zemljiste.entries.toTypedArray()

        val arrayAdapter: ArrayAdapter<Zemljiste> = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            zemljisniTipovi
        )

        zemljisniTipLV.adapter = arrayAdapter
        klimatskiTipLV.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }

    private fun setupDodajJeloBtn() {
        dodajJeloBtn = findViewById(R.id.dodajJeloBtn)

        dodajJeloBtn.setOnClickListener {


            if (!validateEditText(jeloET)) {
                jeloET.error = "Jelo nije validno!"
                return@setOnClickListener
            }

            val novoJelo: String = jeloET.text.toString()

            if (jelaNoveBiljke.contains(novoJelo, true)) {
                jeloET.error = "Jelo vec postoji!"
                return@setOnClickListener
            }

            jelaNoveBiljke.add(novoJelo)

            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                jelaNoveBiljke
            )

            jelaLV.adapter = arrayAdapter

            Toast.makeText(this, "Jelo dodato", Toast.LENGTH_SHORT).show()

        }
    }

    private fun setupJeloET() {

        jeloET = findViewById(R.id.jeloET)

    }

    private fun setupJelaLV() {
        jelaLV = findViewById(R.id.jelaLV)

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            jelaNoveBiljke
        )

        jelaLV.adapter = arrayAdapter

    }

    private fun validateEditText(editText: EditText): Boolean {
        return (editText.text.length < 20) and (editText.text.length > 2)
    }

    private fun MutableList<String>.contains(s: String, ignoreCase: Boolean = false): Boolean {

        return any { it.equals(s, ignoreCase) }
    }

}