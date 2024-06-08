package etf.rma.spirale.mainActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import etf.rma.spirale.biljka.Biljka
import etf.rma.spirale.biljka.KlimatskiTip
import etf.rma.spirale.biljka.MedicinskaKorist
import etf.rma.spirale.biljka.ProfilOkusaBiljke
import etf.rma.spirale.biljka.Zemljiste
import etf.rma.spirale.novaBiljka.NovaBiljkaActivity
import etf.rma.spirale.R
import etf.rma.spirale.defaultBiljke
import etf.rma.spirale.trefleAPI.TrefleDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), BiljkeRVAdapter.RecyclerViewEvent {

    private lateinit var biljkeRecyclerView: RecyclerView
    private lateinit var biljkeRVAdapter: BiljkeRVAdapter

    private lateinit var modSpinner: Spinner
    private lateinit var resetBtn: Button
    private lateinit var novaBiljkaBtn: Button

    private var medicinskiMod: Int = R.layout.medicinski_item
    private var kuharskiMod: Int = R.layout.kuharski_item
    private var botanickiMod: Int = R.layout.botanicki_item

    private var currentMode: Int = medicinskiMod

    private var listFiltered: Boolean = false
    private var filteredBiljke: List<Biljka> = defaultBiljke

    private var slikeBiljaka: MutableMap<String, Bitmap> = mutableMapOf()

    private val novaBiljkaLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                insertNovaBiljka(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getBiljkaBitmaps()

        setupNovaBiljkaBtn()
        setupResetBtn()
        setupModSpinner()
        setupBiljkeRecyclerView()


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getBiljkaBitmaps() {
    val scope = CoroutineScope(Job() + Dispatchers.Main)
    for (biljka in defaultBiljke) {
        scope.launch {
            val image = TrefleDAO().getImage(biljka)
            slikeBiljaka[biljka.naziv] = image
            biljkeRVAdapter.notifyItemChanged(defaultBiljke.indexOf(biljka))
        }
    }
}

    @SuppressLint("NewApi", "NotifyDataSetChanged")
    private fun insertNovaBiljka(it: ActivityResult) {
        val novaBiljka: Biljka = it.data?.getSerializableExtra("novaBiljka", Biljka::class.java)!!

        val scope = CoroutineScope(Job() + Dispatchers.Main)

        scope.launch {
            defaultBiljke.add(TrefleDAO().fixData(novaBiljka))
            listFiltered = false
        }
        scope.launch {
            val image = TrefleDAO().getImage(novaBiljka)
            slikeBiljaka[novaBiljka.naziv] = image
            biljkeRVAdapter.notifyDataSetChanged()
            refreshDisplayedBiljke()
        }
    }

    private fun setupResetBtn() {
        resetBtn = findViewById(R.id.resetBtn)

        resetBtn.setOnClickListener {
            listFiltered = false
            filteredBiljke = defaultBiljke

            refreshDisplayedBiljke()
        }
    }

    private fun setupNovaBiljkaBtn() {
        novaBiljkaBtn = findViewById(R.id.novaBiljkaBtn)

        novaBiljkaBtn.setOnClickListener {
            val intent = Intent(this, NovaBiljkaActivity::class.java)
            // startActivityForResult(intent, REQUEST_CODE)
            novaBiljkaLauncher.launch(intent)
        }
    }

    private fun setupBiljkeRecyclerView() {
        biljkeRecyclerView = findViewById(R.id.biljkeRV)

        biljkeRecyclerView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )

        biljkeRVAdapter = BiljkeRVAdapter(defaultBiljke, slikeBiljaka, this)
        biljkeRVAdapter.setCurrentView(currentMode)
        biljkeRecyclerView.adapter = biljkeRVAdapter
    }

    private fun setupModSpinner() {
        modSpinner = findViewById(R.id.modSpinner)

        ArrayAdapter.createFromResource(
            this, R.array.mod_Spinner_options, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            modSpinner.adapter = adapter
        }

        modSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
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
        biljkeList: List<Biljka>, referenceBiljka: Biljka
    ): List<Biljka> {
        val referenceMedicinskaKorist: Set<MedicinskaKorist> =
            referenceBiljka.medicinskeKoristi.toSet()

        val filteredBiljke = biljkeList.filter { biljka ->
            biljka.medicinskeKoristi.intersect(referenceMedicinskaKorist).isNotEmpty()
        }
        return filteredBiljke
    }

    private fun filterKuharskiBiljke(
        biljkeList: List<Biljka>, referenceBiljka: Biljka
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
        biljkeList: List<Biljka>, referenceBiljka: Biljka
    ): List<Biljka> {
        val referenceKlimatskiTip: Set<KlimatskiTip> = referenceBiljka.klimatskiTipovi.toSet()
        val referenceZemljiste: Set<Zemljiste> = referenceBiljka.zemljisniTipovi.toSet()

        val filteredBiljke = biljkeList.filter { biljka ->
            val biljkaKlimatskiTipSet = biljka.klimatskiTipovi.toSet()
            val biljkaZemljisteSet = biljka.zemljisniTipovi.toSet()
            val hasCommonKlimatskiTip =
                biljkaKlimatskiTipSet.intersect(referenceKlimatskiTip).isNotEmpty()
            val hasCommonZemljiste = biljkaZemljisteSet.intersect(referenceZemljiste).isNotEmpty()
            val hasCommonPorodica = referenceBiljka.porodica == biljka.porodica
            (hasCommonPorodica && hasCommonKlimatskiTip && hasCommonZemljiste)
        }

        return filteredBiljke
    }

    private fun medicinskiClick(referenceBiljka: Biljka) {
        filteredBiljke = filterMedicinskiBiljke(defaultBiljke, referenceBiljka)
        listFiltered = true
        refreshDisplayedBiljke()
    }

    private fun kuharskiClick(referenceBiljka: Biljka) {
        filteredBiljke = filterKuharskiBiljke(defaultBiljke, referenceBiljka)
        listFiltered = true
        refreshDisplayedBiljke()
    }

    private fun botanickiClick(referenceBiljka: Biljka) {
        filteredBiljke = filterBotanickiBiljke(defaultBiljke, referenceBiljka)
        listFiltered = true
        refreshDisplayedBiljke()
    }

    override fun onItemClick(position: Int) {


        val clickedBiljka: Biljka = if (listFiltered) filteredBiljke[position]
        else defaultBiljke[position]

        when (currentMode) {
            medicinskiMod -> medicinskiClick(clickedBiljka)
            kuharskiMod -> kuharskiClick(clickedBiljka)
            botanickiMod -> botanickiClick(clickedBiljka)
        }

    }

}