package etf.rma.spirale.novaBiljka

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import etf.rma.spirale.biljka.Biljka
import etf.rma.spirale.biljka.KlimatskiTip
import etf.rma.spirale.biljka.MedicinskaKorist
import etf.rma.spirale.biljka.ProfilOkusaBiljke
import etf.rma.spirale.biljka.Zemljiste
import etf.rma.spirale.R
import java.io.File

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

    private lateinit var dodajBiljkuBtn: Button
    private lateinit var uslikajBiljkuBtn: Button
    private lateinit var dodajJeloBtn: Button

    private lateinit var slikaIV: ImageView

    private var jelaNoveBiljke: MutableList<String> = mutableListOf()
    private lateinit var profilOkusaNoveBiljke: ProfilOkusaBiljke

    private lateinit var imageURI: Uri
    private val takePhotoContract =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            slikaIV.setImageURI(null)
            slikaIV.setImageURI(imageURI)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nova_biljka)

        setupNazivET()
        setupPorodicaET()
        setupMedicinskoUpozorenjeET()
        setupJeloET()

        setupSlikaIV()

        setupJelaLV()
        setupMedicinskaKoristLV()
        setupKlimatskiTipLV()
        setupZemljisniTipLV()
        setupProfilOkusaLV()

        setupDodajJeloBtn()
        setupDodajBiljkuBtn()
        setupUslikajBiljkuBtn()
    }

    private fun setupMedicinskaKoristLV() {
        medicinskaKoristLV = findViewById(R.id.medicinskaKoristLV)

        val medicinskeKoristiString: List<String> = MedicinskaKorist.getOpisList()

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            medicinskeKoristiString.toTypedArray()
        )

        medicinskaKoristLV.adapter = arrayAdapter
        medicinskaKoristLV.choiceMode = ListView.CHOICE_MODE_MULTIPLE

    }

    private fun setupKlimatskiTipLV() {
        klimatskiTipLV = findViewById(R.id.klimatskiTipLV)

        val klimatskiTipoviString: List<String> = KlimatskiTip.getOpisList()

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            klimatskiTipoviString.toTypedArray()
        )
        klimatskiTipLV.adapter = arrayAdapter
        klimatskiTipLV.choiceMode = ListView.CHOICE_MODE_MULTIPLE

    }

    private fun setupZemljisniTipLV() {
        zemljisniTipLV = findViewById(R.id.zemljisniTipLV)

        val zemljisniTipoviString: List<String> = Zemljiste.getOpisList()

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            zemljisniTipoviString.toTypedArray()
        )

        zemljisniTipLV.adapter = arrayAdapter
        zemljisniTipLV.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }

    private fun setupProfilOkusaLV() {
        profilOkusaLV = findViewById(R.id.profilOkusaLV)

        val profiliOkusaString: List<String> = ProfilOkusaBiljke.getOpisList()

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_list_item_single_choice, profiliOkusaString.toTypedArray()
        )

        profilOkusaLV.adapter = arrayAdapter
        profilOkusaLV.choiceMode = ListView.CHOICE_MODE_SINGLE
        profilOkusaLV.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            profilOkusaNoveBiljke = ProfilOkusaBiljke.entries[position]
        }


    }

    private fun setupDodajJeloBtn() {
        dodajJeloBtn = findViewById(R.id.dodajJeloBtn)

        dodajJeloBtn.setOnClickListener {

            if (!validacijaEditText(jeloET)) {
                return@setOnClickListener
            }

            val novoJelo: String = jeloET.text.toString()

            if (jelaNoveBiljke.contains(novoJelo, true)) {
                jeloET.error = "Jelo vec postoji!"
                return@setOnClickListener
            }

            jelaNoveBiljke.add(novoJelo)

            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
                this, android.R.layout.simple_list_item_1, jelaNoveBiljke
            )

            jelaLV.adapter = arrayAdapter

            Toast.makeText(this, "Jelo dodato", Toast.LENGTH_SHORT).show()

            jeloET.text.clear()

        }
    }

    private fun dodajJeloBtnEditMode(position: Int) {

        dodajJeloBtn.text = getString(R.string.dodajJeloBtn_izmijeniJelo)

        val odabranoJelo: String = jelaNoveBiljke[position]

        jelaNoveBiljke[position] = ""

        jeloET.setText(odabranoJelo)

        dodajJeloBtn.setOnClickListener {

            if (!validacijaEditText(jeloET)) {
                return@setOnClickListener
            }


            val novoJelo: String = jeloET.text.toString()

            if (novoJelo == jelaNoveBiljke[position]) {

                jeloET.text.clear()
                dodajJeloBtn.text = getString(R.string.dodajJeloBtn_dodajJelo)
            }

            if (jelaNoveBiljke.contains(novoJelo, true)) {
                jeloET.error = "Jelo vec postoji!"
                return@setOnClickListener
            }

            jelaNoveBiljke[position] = novoJelo

            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
                this, android.R.layout.simple_list_item_1, jelaNoveBiljke
            )

            jelaLV.adapter = arrayAdapter

            Toast.makeText(this, "Jelo izmijenjeno", Toast.LENGTH_SHORT).show()

            jeloET.text.clear()
            dodajJeloBtn.text = getString(R.string.dodajJeloBtn_dodajJelo)

            setupDodajJeloBtn()

        }
    }

    private fun setupDodajBiljkuBtn() {
        dodajBiljkuBtn = findViewById(R.id.dodajBiljkuBtn)

        dodajBiljkuBtn.setOnClickListener {
            val editTextovi = listOf(nazivET, porodicaET, medicinskoUpozorenjeET)

            var sveIspravno = true

            for (et in editTextovi) {
                if (!validacijaEditText(et)) sveIspravno = false
            }

            if (jelaLV.adapter.count == 0) {
                dodajBiljkuBtn.error = "Nije uneseno ni jedno jelo"
                sveIspravno = false
            }

            if (medicinskaKoristLV.checkedItemCount == 0) {
                dodajBiljkuBtn.error = "Nije odabrana ni jedna korist"
                sveIspravno = false
            }

            if (profilOkusaLV.checkedItemCount == 0) {
                dodajBiljkuBtn.error = "Nije odabran profil okusa"
                sveIspravno = false
            }

            if (klimatskiTipLV.checkedItemCount == 0) {
                dodajBiljkuBtn.error = "Nije odabran ni jedan klimatski tip"
                sveIspravno = false
            }

            if (zemljisniTipLV.checkedItemCount == 0) {
                dodajBiljkuBtn.error = "Nije odabran ni jedan tip zemljista"
                sveIspravno = false
            }

            if (!sveIspravno) {
                Toast.makeText(this, "Nije odabrano dovoljno opcija", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Uspješno spašena biljka!", Toast.LENGTH_SHORT).show()

            val novaBiljka: Biljka = buildReturnBiljka()
            val resultIntent = Intent()
            resultIntent.putExtra("novaBiljka", novaBiljka)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()

        }
    }

    private fun setupUslikajBiljkuBtn() {
        uslikajBiljkuBtn = findViewById(R.id.uslikajBiljkuBtn)

        uslikajBiljkuBtn.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "Camera permision not granted!", Toast.LENGTH_SHORT).show()
                requestPermissions(
                    arrayOf(android.Manifest.permission.CAMERA), 400
                )
            }

            takePhotoContract.launch(imageURI)
        }
    }

    private fun setupJeloET() {
        jeloET = findViewById(R.id.jeloET)
    }

    private fun setupPorodicaET() {
        porodicaET = findViewById(R.id.porodicaET)
    }

    private fun setupMedicinskoUpozorenjeET() {
        medicinskoUpozorenjeET = findViewById(R.id.medicinskoUpozorenjeET)
    }

    private fun setupNazivET() {
        nazivET = findViewById(R.id.nazivET)
    }

    private fun setupJelaLV() {
        jelaLV = findViewById(R.id.jelaLV)

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, jelaNoveBiljke
        )

        jelaLV.adapter = arrayAdapter

        jelaLV.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

            dodajJeloBtnEditMode(position)

        }

    }

    private fun setupSlikaIV() {

        imageURI = createImageURI()

        slikaIV = findViewById(R.id.slikaIV)
        slikaIV.setImageResource(R.mipmap.plant_sample_foreground)
    }

    private fun validacijaEditText(editText: EditText): Boolean {

        if ((editText.text.length > 20)) {
            editText.error = "Sadrzaj predug"
            return false
        }


        if (editText.text.length < 2) {
            editText.error = "Sadrzaj prekratak"
            return false
        }

        return true
    }

    private fun MutableList<String>.contains(s: String, ignoreCase: Boolean = false): Boolean {
        return any { it.equals(s, ignoreCase) }
    }

    private fun buildReturnBiljka(): Biljka {

        val nazivNoveBiljke = nazivET.text.toString()
        val porodicaNoveBiljke = porodicaET.text.toString()
        val upozorenjeNoveBiljke = medicinskoUpozorenjeET.text.toString()

        val medKoristiNoveBiljke = getSelectedItems(medicinskaKoristLV, MedicinskaKorist.entries)
        val klimeNoveBiljke = getSelectedItems(klimatskiTipLV, KlimatskiTip.entries)
        val zemljistaNoveBiljke = getSelectedItems(zemljisniTipLV, Zemljiste.entries)

        return Biljka(
            nazivNoveBiljke,
            porodicaNoveBiljke,
            upozorenjeNoveBiljke,
            medKoristiNoveBiljke,
            profilOkusaNoveBiljke,
            jelaNoveBiljke,
            klimeNoveBiljke,
            zemljistaNoveBiljke
        )
    }

    private fun <T> getSelectedItems(listView: ListView, enumEntries: List<T>): List<T> {
        val selectedItems = mutableListOf<T>()

        // Get the SparseBooleanArray of checked items
        val checkedItems = listView.checkedItemPositions

        // Iterate through the items and add the checked ones to the list
        for (i in 0 until checkedItems.size()) {
            val position = checkedItems.keyAt(i)
            if (checkedItems.valueAt(i)) {
                selectedItems.add(enumEntries[position])
            }
        }
        return selectedItems
    }

    private fun createImageURI(): Uri {
        val image = File(filesDir, "camera_photos.png")
        return FileProvider.getUriForFile(this, "etf.rma.spirale.FileProvider", image)
    }
}