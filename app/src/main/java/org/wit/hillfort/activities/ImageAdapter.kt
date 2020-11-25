package org.wit.hillfort.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_image.view.*
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImageFromPath


// Listener for click on an image
interface ImageListener{
    fun onImageClick(image: String)
}

// Adapter for a list of images and a listner
class ImageAdapter(private var images: ArrayList<String>,
                   private val listener: ImageListener
) :

    // Recycler View to show a list of images
    RecyclerView.Adapter<ImageAdapter.MainHolder>() {


    // create the view holder for the image card
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {

        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_image,
                parent,
                false
            )
        )
    }

    // bind the image and the listener
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val image = images[holder.adapterPosition]
        holder.bind(image, listener)
    }


    //    Gets the number of images we need to display
    override fun getItemCount(): Int = images.size

    // bind each image to the image card & set the click listener on each image
    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(image: String, listener: ImageListener) {

            itemView.hillfortImage.setImageBitmap(
                readImageFromPath(
                    itemView.context,
                    image
                )
            )
            itemView.setOnClickListener{listener.onImageClick(image)}
        }

    }
}
