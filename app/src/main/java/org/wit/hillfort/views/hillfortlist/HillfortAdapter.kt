package org.wit.hillfort.activities

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.card_hillfort.*
import kotlinx.android.synthetic.main.card_hillfort.view.*
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel

// Listener for clicks on a hillfort card or on the checkbox for visited hillforts
interface HillfortListener {
    fun onHillfortClick(hillfort: HillfortModel)
    fun onVisitedCheckboxClick(hillfort: HillfortModel, isChecked: Boolean)
    fun onFavouriteButtonClick(hillfort: HillfortModel)
}

// Hillfort Adapter for recycler view of hillforts
class HillfortAdapter constructor(private var hillforts: List<HillfortModel>,
                                    private val listener: HillfortListener) :
    RecyclerView.Adapter<HillfortAdapter.MainHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_hillfort,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val hillfort = hillforts[holder.adapterPosition]
        holder.bind(hillfort, listener)
    }

    override fun getItemCount(): Int = hillforts.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // for each hillfort bind values to the hillfort card
        fun bind(hillfort: HillfortModel, listener: HillfortListener) {
            itemView.hillfortName.text = hillfort.name
            itemView.hillfortListLat.text = (hillfort.location.lat.toString()+" lat")
            itemView.hillfortListLng.text = (hillfort.location.lng.toString()+" lng")
            // icon image on the hillfort list
            if (hillfort.images.size >0)
            {
                Glide.with(itemView.context).load(hillfort.images[0]).into(itemView.imageIcon);
            }
            if (hillfort.visited){
                itemView.visitedCard.isChecked = true
            }
            if(hillfort.favourite)
            {
                itemView.btnFavourite.setColorFilter(Color.RED);
            }
            else itemView.btnFavourite.setColorFilter(Color.LTGRAY);

            itemView.setOnClickListener{listener.onHillfortClick(hillfort)}
            itemView.visitedCard.setOnClickListener{listener.onVisitedCheckboxClick(hillfort, itemView.visitedCard.isChecked)}
            itemView.btnFavourite.setOnClickListener{listener.onFavouriteButtonClick(hillfort)}
        }
    }
}