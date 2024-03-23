package etf.rma.spirale

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var biljkeRVAdapter: BiljkeRVAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.biljkeRV)

        recyclerView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )

        biljkeRVAdapter = BiljkeRVAdapter(biljke)
        biljkeRVAdapter.setCurrentView(R.layout.kuharski_item)
        recyclerView.adapter = biljkeRVAdapter

    }
}