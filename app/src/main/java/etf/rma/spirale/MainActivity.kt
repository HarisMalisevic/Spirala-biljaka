package etf.rma.spirale

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {


    private lateinit var biljkeRecyclerView: RecyclerView
    private lateinit var biljkeRVAdapter: BiljkeRVAdapter

    private lateinit var modSpinner: Spinner
    private lateinit var resetBtn: Button

    private var medicinskiMod: Int = R.layout.medicinski_item
    private var kuharskiMod: Int = R.layout.kuharski_item
    private var botanickiMod: Int = R.layout.botanicki_item

    private var currentMode: Int = medicinskiMod

    private var listFiltered: Boolean = false
    private var filteredBiljke: List<Biljka> = defaultBiljke

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupResetBtn()
        setupModSpinner()
        setupBiljkeRecyclerView()

        listFiltered = true
        filteredBiljke = filterKuharskiBiljke(defaultBiljke, defaultBiljke[0])

        refreshDisplayedBiljke()

    }

    private fun setupResetBtn() {
        resetBtn = findViewById<Button>(R.id.resetBtn)

        resetBtn.setOnClickListener{
            listFiltered = false
            filteredBiljke = defaultBiljke

            refreshDisplayedBiljke()
        }
    }

    private fun setupBiljkeRecyclerView() {
        biljkeRecyclerView = findViewById(R.id.biljkeRV)

        biljkeRecyclerView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )

        biljkeRVAdapter = BiljkeRVAdapter(defaultBiljke)
        biljkeRVAdapter.setCurrentView(currentMode)
        biljkeRecyclerView.adapter = biljkeRVAdapter
    }

    private fun setupModSpinner() {
        modSpinner = findViewById(R.id.modSpinner)

        ArrayAdapter.createFromResource(
            this,
            R.array.mod_Spinner_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            modSpinner.adapter = adapter
        }

        modSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                currentMode = when (position) {
                    0 -> medicinskiMod
                    1 -> kuharskiMod
                    2 -> botanickiMod
                    else -> medicinskiMod
                }
                biljkeRVAdapter.setCurrentView(currentMode)

                refreshDisplayedBiljke()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun refreshDisplayedBiljke() {
        if (listFiltered) {
            biljkeRVAdapter.updateBiljke(filteredBiljke)
            return
        }
        biljkeRVAdapter.updateBiljke(defaultBiljke)
    }

    private fun filterMedicinskiBiljke(
        biljkeList: List<Biljka>,
        referenceBiljka: Biljka
    ): List<Biljka> {
        val referenceMedicinskaKorist: Set<MedicinskaKorist> =
            referenceBiljka.medicinskeKoristi.toSet()

        val filteredBiljke = biljkeList.filter { biljka ->
            biljka.medicinskeKoristi.intersect(referenceMedicinskaKorist).isNotEmpty()
        }

        return filteredBiljke
    }

    private fun filterKuharskiBiljke(
        biljkeList: List<Biljka>,
        referenceBiljka: Biljka
    ): List<Biljka> {
        val referenceJela: Set<String> = referenceBiljka.jela.toSet()
        val referenceProfilOkusa: ProfilOkusaBiljke = referenceBiljka.profilOkusa

        val filteredBiljke = biljkeList.filter { biljka ->
            val biljkaJelaSet = biljka.jela.toSet()
            val hasCommonJela = biljkaJelaSet.intersect(referenceJela).isNotEmpty()
            val hasSameProfilOkusa = (biljka.profilOkusa == referenceProfilOkusa)
            (hasCommonJela || hasSameProfilOkusa)
        }

        return filteredBiljke
    }

    private fun filterBotanickiBiljke(
        biljkeList: List<Biljka>,
        referenceBiljka: Biljka
    ): List<Biljka> {
        val referenceKlimatskiTip: Set<KlimatskiTip> = referenceBiljka.klimatskiTipovi.toSet()
        val referenceZemljiste: Set<Zemljiste> = referenceBiljka.zemljisniTipovi.toSet()

        val filteredBiljke = biljkeList.filter { biljka ->
            val biljkaJelaSet = biljka.jela.toSet()
            val hasCommonKlimatskiTip = biljkaJelaSet.intersect(referenceKlimatskiTip).isNotEmpty()
            val hasCommonZemljiste = biljkaJelaSet.intersect(referenceZemljiste).isNotEmpty()
            (hasCommonKlimatskiTip || hasCommonZemljiste)
        }

        return filteredBiljke
    }

}