package com.example.perludilindungi.ui.lokasi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.perludilindungi.R
import com.example.perludilindungi.network.FaskesProperty
import timber.log.Timber

class LokasiAdapter(private val obj : FaskesProperty): RecyclerView.Adapter<LokasiAdapter.ViewHolder>(){

    interface ClickListener {
        fun onItemClick(position: Int, v: View?)
        fun onItemLongClick(position: Int, v: View?)
    }
    private var clickListener: ClickListener? = null

    override fun onBindViewHolder(holder: LokasiAdapter.ViewHolder, position: Int) {
        val namaView = holder.namaFaskes
        val alamatView = holder.alamat
        val jenisView = holder.jenisFaskes
        val telpView = holder.noTelp
        val kodeView = holder.kodeFaskes

        val data = obj.data?.get(position)
        if (data != null) {
            namaView.text = data.nama
            alamatView.text = data.alamat
            jenisView.text = data.jenis_faskes
            telpView.text = data.telp
            kodeView.text = "KODE:" + data.kode
        }

    }
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(
                R.layout.list_item_lokasi,
                parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return obj.count_total
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        val namaFaskes: TextView = itemView.findViewById(R.id.namaFaskes)
        val alamat: TextView = itemView.findViewById(R.id.alamat)
        val jenisFaskes: TextView = itemView.findViewById(R.id.jenisFaskes)
        val noTelp: TextView = itemView.findViewById(R.id.noTelp)
        val kodeFaskes: TextView = itemView.findViewById(R.id.kodeFaskes)

        override fun onClick(v: View) {
            clickListener?.onItemClick(adapterPosition, v)
            Timber.i("Click")
        }

        override fun onLongClick(v: View): Boolean {
            clickListener?.onItemLongClick(adapterPosition, v)
            Timber.i("Long Click")
            return false
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }
    }
    fun setOnItemClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }
}
