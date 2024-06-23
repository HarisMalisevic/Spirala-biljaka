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
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import etf.rma.spirale.App
import etf.rma.spirale.biljka.Biljka
import etf.rma.spirale.biljka.KlimatskiTip
import etf.rma.spirale.biljka.MedicinskaKorist
import etf.rma.spirale.biljka.ProfilOkusaBiljke
import etf.rma.spirale.biljka.Zemljiste
import etf.rma.spirale.novaBiljka.NovaBiljkaActivity
import etf.rma.spirale.R
import etf.rma.spirale.dataPersistence.database.BiljkaDatabase
import etf.rma.spirale.defaultBiljke
import etf.rma.spirale.dataPersistence.trefleAPI.TrefleDAO
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
    private lateinit var filteredBiljke: List<Biljka>

    private var slikeDefaultBiljaka: MutableMap<String, Bitmap> = mutableMapOf()

    private val novaBiljkaLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                insertNovaBiljka(it)
            }
        }

    private lateinit var bottomBar: ConstraintLayout
    private lateinit var brzaPretragaBtn: Button
    private lateinit var bojaSpinner: Spinner
    private lateinit var pretragaET: EditText

    private val trefleDAO = TrefleDAO(App.context)

    private lateinit var biljkaDAO: BiljkaDatabase.BiljkaDAO


    private var blockFiltering: Boolean = false

    private var radnaLista = mutableListOf<Biljka>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        biljkaDAO = BiljkaDatabase.getInstance(this).biljkaDao()

        lifecycleScope.launch {

            val biljkeIzBaze = biljkaDAO.getAllBiljkas().toMutableList()

            if (biljkeIzBaze.isEmpty()) {
                biljkaDAO.insertAll(defaultBiljke)
                radnaLista = defaultBiljke.toMutableList()
            } else {
                radnaLista = biljkeIzBaze
            }

            val job2 = launch {
                setupBiljkeRecyclerView()
                setupNovaBiljkaBtn()
                setupResetBtn()
                setupModSpinner()
                setupBottomBar()
                filteredBiljke = radnaLista
                refreshDisplayedBiljke()
            }

            val job3 = launch {
                getBiljkaBitmaps()
            }

            job2.join()
            job3.join()

        }


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getBiljkaBitmaps() {

        for (biljka in radnaLista) {
            lifecycleScope.launch {
                val image = trefleDAO.getImage(biljka)


                val job2 = launch {
                    slikeDefaultBiljaka[biljka.naziv] = image
                    biljkaDAO.addImage(biljka.id!!, image)

                    biljkeRVAdapter.notifyItemChanged(radnaLista.indexOf(biljka))
                }
                job2.join()
            }
        }
    }

    @SuppressLint("NewApi", "NotifyDataSetChanged")
    private fun insertNovaBiljka(it: ActivityResult) {
        val novaBiljka: Biljka = it.data?.getSerializableExtra("novaBiljka", Biljka::class.java)!!

        lifecycleScope.launch {

            radnaLista.add(TrefleDAO().fixData(novaBiljka))
            val fixedNovaBiljka = radnaLista.last()

            biljkaDAO.saveBiljka(fixedNovaBiljka)

            listFiltered = false

            val image = trefleDAO.getImage(fixedNovaBiljka)

            val job2 = launch {
                biljkaDAO.addImage(fixedNovaBiljka.id!!, image)
                slikeDefaultBiljaka[fixedNovaBiljka.naziv] = image
                biljkeRVAdapter.notifyDataSetChanged()
                refreshDisplayedBiljke()
            }
            job2.join()

        }

    }

    private fun setupResetBtn() {
        resetBtn = findViewById(R.id.resetBtn)

        resetBtn.setOnClickListener {
            listFiltered = false
            filteredBiljke = defaultBiljke

            pretragaET.text.clear()

            displayRadnaLista()

            refreshDisplayedBiljke()
        }
    }

    private fun setupNovaBiljkaBtn() {
        novaBiljkaBtn = findViewById(R.id.novaBiljkaBtn)

        novaBiljkaBtn.setOnClickListener {
            displayRadnaLista()
            val intent = Intent(this, NovaBiljkaActivity::class.java)
            // startActivityForResult(intent, REQUEST_CODE)
            novaBiljkaLauncher.launch(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun displayRadnaLista() {
        blockFiltering = false
        biljkeRVAdapter.updateBiljke(radnaLista, slikeDefaultBiljaka)
        biljkeRVAdapter.notifyDataSetChanged()
    }

    private fun setupBiljkeRecyclerView() {
        biljkeRecyclerView = findViewById(R.id.biljkeRV)

        biljkeRecyclerView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )

        biljkeRVAdapter = BiljkeRVAdapter(radnaLista, slikeDefaultBiljaka, this)
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

            displayRadnaLista()
        }

        modSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                currentMode = when (position) {
                    0 -> {
                        bottomBar.visibility = View.GONE
                        medicinskiMod
                    }

                    1 -> {
                        bottomBar.visibility = View.GONE
                        kuharskiMod
                    }

                    2 -> {
                        bottomBar.visibility = View.VISIBLE
                        botanickiMod
                    }

                    else -> {
                        bottomBar.visibility = View.GONE
                        medicinskiMod
                    }
                }
                biljkeRVAdapter.setCurrentView(currentMode)

                refreshDisplayedBiljke()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupBottomBar() {
        bottomBar = findViewById(R.id.bottomBar)
        brzaPretragaBtn = findViewById(R.id.brzaPretraga)
        bojaSpinner = findViewById(R.id.bojaSPIN)
        pretragaET = findViewById(R.id.pretragaET)

        ArrayAdapter.createFromResource(
            this, R.array.color_Spinner_options, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            bojaSpinner.adapter = adapter
        }

        brzaPretragaBtn.setOnClickListener {
            val flowerColor = bojaSpinner.selectedItem.toString()
            val substr = pretragaET.text.toString()

            blockFiltering = true


            lifecycleScope.launch {
                val plantsFilteredByFlowerColor =
                    trefleDAO.getPlantsWithFlowerColor(flowerColor, substr)
                biljkeRVAdapter.setBiljke(plantsFilteredByFlowerColor)
                biljkeRVAdapter.notifyDataSetChanged()

                val plantBitmaps = mutableMapOf<String, Bitmap>()
                for (plant in plantsFilteredByFlowerColor) {
                    if (!blockFiltering) break
                    val image = trefleDAO.getImage(plant)
                    plantBitmaps[plant.naziv] = image
                    biljkeRVAdapter.setBitmaps(plantBitmaps)
                    biljkeRVAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun refreshDisplayedBiljke() {
        if (listFiltered) {
            biljkeRVAdapter.updateBiljke(filteredBiljke, slikeDefaultBiljaka)
            return
        }
        biljkeRVAdapter.updateBiljke(radnaLista, slikeDefaultBiljaka)
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
        val referenceProfilOkusa: ProfilOkusaBiljke? = referenceBiljka.profilOkusa

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
        filteredBiljke = filterMedicinskiBiljke(radnaLista, referenceBiljka)
        listFiltered = true
        refreshDisplayedBiljke()
    }

    private fun kuharskiClick(referenceBiljka: Biljka) {
        filteredBiljke = filterKuharskiBiljke(radnaLista, referenceBiljka)
        listFiltered = true
        refreshDisplayedBiljke()
    }

    private fun botanickiClick(referenceBiljka: Biljka) {
        filteredBiljke = filterBotanickiBiljke(radnaLista, referenceBiljka)
        listFiltered = true
        refreshDisplayedBiljke()
    }

    override fun onItemClick(position: Int) {

        if (blockFiltering)
            return


        val clickedBiljka: Biljka = if (listFiltered) filteredBiljke[position]
        else radnaLista[position]

        when (currentMode) {
            medicinskiMod -> medicinskiClick(clickedBiljka)
            kuharskiMod -> kuharskiClick(clickedBiljka)
            botanickiMod -> botanickiClick(clickedBiljka)
        }

    }

}