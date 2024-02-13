package es.salvaaoliiver.secondevproject.main.bottombar.api

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import es.salvaaoliiver.secondevproject.R
import es.salvaaoliiver.secondevproject.main.bottombar.api.`object`.NasaPictureOfDay

class NasaPicturesAdapter(private var images: List<NasaPictureOfDay>) :
    RecyclerView.Adapter<NasaPicturesAdapter.NasaPictureViewHolder>() {

    fun updateImages(newImages: List<NasaPictureOfDay>) {
        images = newImages
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NasaPictureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_nasa, parent, false)
        return NasaPictureViewHolder(view)
    }

    override fun onBindViewHolder(holder: NasaPictureViewHolder, position: Int) {
        val currentImage = images[position]
        holder.bind(currentImage)
    }

    override fun getItemCount(): Int = images.size

    inner class NasaPictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(image: NasaPictureOfDay) {
            Picasso.get().load(image.url).into(imageView)
        }
    }
}
