package com.example.perludilindungi.ui.news

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.perludilindungi.R
import com.example.perludilindungi.network.NewsProperty
import com.squareup.picasso.Picasso
import timber.log.Timber


class NewsAdapter (private val obj : NewsProperty): RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    interface ClickListener {
        fun onItemClick(position: Int, v: View?)
        fun onItemLongClick(position: Int, v: View?)
    }
    private var clickListener: ClickListener? = null

    override fun onBindViewHolder(holder: NewsAdapter.ViewHolder, position: Int) {
        val headlineView = holder.headline
        val dateView = holder.date
        val thumbnailView = holder.thumbnail
        headlineView.text = obj.results?.get(position)?.title
        val idx= obj.results?.get(position)?.pubDate?.indexOf(":")
        if (idx != null) {
            dateView.text = obj.results?.get(position)?.pubDate?.subSequence(0, idx-3) ?: obj.results?.get(position)?.pubDate
        } else{
            dateView.text  =obj.results?.get(position)?.pubDate
        }
        val url : Uri = Uri.parse(obj.results?.get(position)?.image?.imageUrl)
        Timber.i(url.toString())
        Picasso.get().load(url).into(thumbnailView)

    }
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(
                R.layout.list_item_news,
                parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return obj.count
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, OnLongClickListener{
        val headline: TextView = itemView.findViewById(R.id.news_headline)
        val date: TextView = itemView.findViewById(R.id.news_date)
        val thumbnail: ImageView = itemView.findViewById(R.id.news_image)
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

