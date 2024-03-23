package etf.rma.spirale

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class BiljkeRVAdapter(private var biljke: List<Biljka>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var medicinskiView: Int = R.layout.medicinski_item
    private var kuharskiView: Int = R.layout.kuharski_item
    private var botanickiView: Int = R.layout.botanicki_item

    private var currentView = medicinskiView


    public fun setCurrentView(value: Int) {
            this.currentView = value
        }


    override fun getItemViewType(position: Int): Int {
        return currentView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {
            medicinskiView -> {
                val view: View =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.medicinski_item, parent, false)

                return MedicinskiViewHolder(view)
            }

            kuharskiView -> {
                val view: View =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.kuharski_item, parent, false)
                return KuharskiViewHolder(view)
            }

            botanickiView -> {
                val view: View =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.botanicki_item, parent, false)

                return BotanickiViewHolder(view)
            }

            else -> {
                throw IllegalArgumentException("View not recognized!")
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item: Biljka = biljke[position]

        when (currentView) {
            medicinskiView -> {
                val medicinskiVH: MedicinskiViewHolder = holder as MedicinskiViewHolder
                medicinskiVH.naziv.text = item.naziv
                medicinskiVH.upozorenje.text = item.medicinskoUpozorenje
                medicinskiVH.korist1.text = item.medicinskeKoristi[0].opis
                medicinskiVH.korist2.text = item.medicinskeKoristi[1].opis
                //medicinskiVH.korist3.text = item.medicinskeKoristi[2].opis

                // TODO: Snaci se sa manjim brojem koristi
            }

            kuharskiView -> {
                val kuharskiVH: KuharskiViewHolder = holder as KuharskiViewHolder
                kuharskiVH.naziv.text = item.naziv
                kuharskiVH.profilOkusa.text = item.medicinskoUpozorenje
                kuharskiVH.jelo1.text = item.jela[0]
                //kuharskiVH.jelo2.text = item.jela[1]
                //kuharskiVH.jelo3.text = item.jela[2]

                // TODO: Snaci se sa manjim brojem jela
            }

            botanickiView -> {
                val botanickiVH: BotanickiViewHolder = holder as BotanickiViewHolder
                botanickiVH.naziv.text = item.naziv
                botanickiVH.porodica.text = item.porodica
                botanickiVH.klimatskiTip.text = item.klimatskiTipovi[0].opis
                botanickiVH.zemljisniTip.text = item.zemljisniTipovi[0].naziv
            }

        }
    }

    override fun getItemCount(): Int {
        return biljke.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateBiljke(biljke: List<Biljka>) {
        this.biljke = biljke
        this.notifyDataSetChanged()
    }


    inner class MedicinskiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val naziv: TextView = itemView.findViewById(R.id.nazivItem)
        var upozorenje: TextView = itemView.findViewById(R.id.upozorenjeItem)
        var korist1: TextView = itemView.findViewById(R.id.korist1Item)
        var korist2: TextView = itemView.findViewById(R.id.korist2Item)
        var korist3: TextView = itemView.findViewById(R.id.korist3Item)
    }

    inner class KuharskiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val naziv: TextView = itemView.findViewById(R.id.nazivItem)
        var profilOkusa: TextView = itemView.findViewById(R.id.profilOkusaItem)
        var jelo1: TextView = itemView.findViewById(R.id.jelo1Item)
        var jelo2: TextView = itemView.findViewById(R.id.jelo2Item)
        var jelo3: TextView = itemView.findViewById(R.id.jelo3Item)
    }

    inner class BotanickiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val naziv: TextView = itemView.findViewById(R.id.nazivItem)
        var porodica: TextView = itemView.findViewById(R.id.porodicaItem)
        var klimatskiTip: TextView = itemView.findViewById(R.id.klimatskiTipItem)
        var zemljisniTip: TextView = itemView.findViewById(R.id.zemljisniTipItem)
    }
}
