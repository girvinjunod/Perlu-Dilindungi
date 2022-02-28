package com.example.perludilindungi.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.perludilindungi.R
import com.example.perludilindungi.network.FaskesProperty
import timber.log.Timber

class DetailAdapter(private val obj : FaskesProperty): RecyclerView.Adapter<DetailAdapter.ViewHolder>(){

    interface ClickListener {
        fun onItemClick(position: Int, v: View?)
        fun onItemLongClick(position: Int, v: View?)
    }

    private var clickListener: DetailAdapter.ClickListener? = null

    override fun onBindViewHolder(holder: DetailAdapter.ViewHolder, position: Int) {
        val namaView = holder.namaFaskes
        val alamatView = holder.alamat
        val jenisView = holder.jenisFaskes
        val telpView = holder.noTelp
        val kodeView = holder.kodeFaskes
        val statusView = holder.statusFaskes

        val data = obj.data?.get(position)
        if (data != null) {
            namaView.text = data.nama
            alamatView.text = data.alamat
            jenisView.text = data.jenis_faskes
            telpView.text = data.telp
            kodeView.text = "KODE:" + data.kode
            statusView.text = "Fasilitas ini\n" + holder.statusFaskes
        }

    }
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int): DetailAdapter.ViewHolder {
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
        val statusFaskes : TextView = itemView.findViewById(R.id.statusFaskes)

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
    fun setOnItemClickListener(clickListener: DetailAdapter.ClickListener) {
        this.clickListener = clickListener
    }
}