package com.example.perludilindungi.ui.bookmark

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.perludilindungi.R
import com.example.perludilindungi.room.Bookmark
import com.example.perludilindungi.ui.lokasi.LokasiAdapter
import timber.log.Timber

class BookmarkAdapter(private val bookmarks: List<Bookmark>) : RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {

    interface ClickListener {
        fun onItemClick(position: Int, v: View?)
        fun onItemLongClick(position: Int, v: View?)
    }
    private var clickListener: ClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        return BookmarkViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_lokasi, parent, false)
        )
    }

    override fun getItemCount() = bookmarks.size

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val bookmark = bookmarks[position]

        val namaView = holder.namaFaskes
        val alamatView = holder.alamat
        val jenisView = holder.jenisFaskes
        val telpView = holder.noTelp
        val kodeView = holder.kodeFaskes

        if (bookmark != null) {
            namaView.text = bookmark.namaFaskes
            alamatView.text = bookmark.alamatFaskes
            jenisView.text = bookmark.jenisFaskes
            telpView.text = bookmark.telpFaskes
            kodeView.text = "KODE:" + bookmark.kodeFaskes
            if (jenisView.text.toString() == "PUSKESMAS"){
                jenisView.setBackgroundColor(Color.parseColor("#ff80ff"))
                jenisView.setTextColor(Color.WHITE)
            }else if (jenisView.text.toString() == "RUMAH SAKIT"){
                jenisView.setBackgroundColor(Color.parseColor("#00DDFF"))
                jenisView.setTextColor(Color.WHITE)
            } else if (jenisView.text.toString() == "KLINIK"){
                jenisView.setBackgroundColor(Color.parseColor("#FFF800"))
                jenisView.setTextColor(Color.WHITE)
            }
        }
    }

    inner class BookmarkViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
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