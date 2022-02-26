package com.example.perludilindungi.ui.news

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.perludilindungi.R
import com.example.perludilindungi.network.NewsProperty

class NewsAdapter (private val obj : NewsProperty): RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

//    var data = NewsProperty
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }
//

    override fun onBindViewHolder(holder: NewsAdapter.ViewHolder, position: Int) {
        val headlineView = holder.headline
        val dateView = holder.date
        val thumbnailView = holder.thumbnail
        headlineView.text = obj.results?.get(position)?.title
        dateView.text = obj.results?.get(position)?.pubDate
        val url : Uri = Uri.parse("obj.results[position].image.imageUrl")
        thumbnailView.setImageURI(url)

    }
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.list_item_news,
                parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return obj.count
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val headline: TextView = itemView.findViewById(R.id.news_headline)
        val date: TextView = itemView.findViewById(R.id.news_date)
        val thumbnail: ImageView = itemView.findViewById(R.id.news_image)
    }
}