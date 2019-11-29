package com.github.admitrevskiy.images.ui.helpers

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.github.admitrevskiy.images.R
import com.github.admitrevskiy.images.model.Photo

class ImagesAdapter(private val photos: List<Photo>,
                    sizeListener: () -> Unit
) : RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    private val tag = "ImagesAdapter"
    private val loadController = RecyclerLoadController(sizeListener, ::getItemCount)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = photos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        photos[position].apply {
            Log.d(tag, "Load image. Position: $position; URL: ${this.source}")
            holder.rover.text = this.rover.name
            holder.placeAndDate.apply {
                text = this.context.getString(R.string.mars_time, earthDate, sol)
            }

            Glide.with(holder.imageView)
                .load(this.source)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)            // Cache images to disk
                .apply(RequestOptions().override(300, 400))  // Reduce quality
                .thumbnail(0.5f)
                .placeholder(R.drawable.ic_mars)
                .listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?,
                                              model: Any?,
                                              target: Target<Drawable>?,
                                              isFirstResource: Boolean): Boolean {

                        loadController.onLoadError(position, e)
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?,
                                                 model: Any?,
                                                 target: Target<Drawable>?,
                                                 dataSource: DataSource?,
                                                 isFirstResource: Boolean): Boolean {

                        loadController.onLoad(position)
                        return false
                    }
                })
                .into(holder.imageView)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image)
        val rover: TextView = itemView.findViewById(R.id.rover_name)
        val placeAndDate: TextView = itemView.findViewById(R.id.place_and_date)
    }
}


