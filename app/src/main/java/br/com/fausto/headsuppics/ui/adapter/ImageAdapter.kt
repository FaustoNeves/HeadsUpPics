package br.com.fausto.headsuppics.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.fausto.headsuppics.R
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton

class ImageAdapter(
    private val urls: List<String>,
    private val deleteClickListener: (String) -> Unit,
    private val downloadClickListener: (String) -> Unit
) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(
            url: String,
            deleteClickListener: (String) -> Unit,
            downloadClickListener: (String) -> Unit
        ) {
            Glide.with(itemView).load(url).into(itemView.findViewById(R.id.ivImage))
            itemView.findViewById<MaterialButton>(R.id.deleteButton).setOnClickListener {
                deleteClickListener(url)
            }
            itemView.findViewById<MaterialButton>(R.id.downloadButton).setOnClickListener {
                downloadClickListener(url)
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
        holder.bindView(urls[position], deleteClickListener, downloadClickListener)
    }

    override fun getItemCount() = urls.size
}