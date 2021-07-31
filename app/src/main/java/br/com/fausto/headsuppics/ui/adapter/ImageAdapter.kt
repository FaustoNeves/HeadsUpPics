package br.com.fausto.headsuppics.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import br.com.fausto.headsuppics.R
import com.bumptech.glide.Glide

class ImageAdapter(private val urls: List<String>, private val clickListener: (String) -> Unit) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(url: String, clickListener: (String) -> Unit) {
            Glide.with(itemView).load(url).into(itemView.findViewById(R.id.ivImage))
            itemView.findViewById<ImageView>(R.id.ivImage).setOnClickListener {
                clickListener(url)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_image,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bindView(urls[position], clickListener)
    }

    override fun getItemCount() = urls.size
}