package com.fp.fproject.Activity.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.fp.fproject.R
import android.widget.TextView
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageView
import typ

class typAdapter(
    private val context: Context,
    private var typList: List<typ>,
    private val listener: RecyclerViewClickListener,
    crateItems: MutableList<typ>
) : RecyclerView.Adapter<typAdapter.typViewHolder>() {

    // Function to update typList
    fun updateData(newTypList: List<typ>) {
        typList = newTypList
        notifyDataSetChanged() // Notify adapter of data change
    }

    inner class typViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var images: ImageView
        var fav_icon: ImageView
        var vrf_icon: ImageView
        var name: TextView
        var feed_txt: TextView
        var by_txt: TextView
        var dlv_txt: TextView
        var tm_mc: TextView
        var feu1: TextView
        var feu2: TextView
        var feu3: TextView
        val addToCart: Button = itemView.findViewById(R.id.addToCart)

        init {
            images = itemView.findViewById(R.id.images)
            fav_icon = itemView.findViewById(R.id.fav_icon)
            vrf_icon = itemView.findViewById(R.id.vrf_icon)
            name = itemView.findViewById(R.id.name)
            feed_txt = itemView.findViewById(R.id.feed_txt)
            by_txt = itemView.findViewById(R.id.by_txt)
            dlv_txt = itemView.findViewById(R.id.dlv_txt)
            tm_mc = itemView.findViewById(R.id.tm_mc)
            feu1 = itemView.findViewById(R.id.feu1)
            feu2 = itemView.findViewById(R.id.feu2)
            feu3 = itemView.findViewById(R.id.feu3)

            itemView.tag = adapterPosition
            // Set click listener using the extension function
            addToCart.setOnClickListenerWithPosition { position ->
                // Handle click event here
                listener.onClick(itemView, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): typViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.food_row_one, parent, false)
        return typViewHolder(view)
    }

    override fun onBindViewHolder(holder: typViewHolder, position: Int) {
        val currentItem = typList[position]
        holder.name.text = currentItem.name
        holder.images.setImageResource(currentItem.imageResource ?: R.drawable.mc_feu)
        holder.fav_icon.setImageResource(currentItem.favoriteIcon ?: R.drawable.favourite_icon)
        holder.vrf_icon.setImageResource(currentItem.verificationIcon ?: R.drawable.verf_mc)
        holder.feed_txt.text = currentItem.rating
        holder.by_txt.text = currentItem.reviews
        holder.dlv_txt.text = currentItem.deliveryInfo
        holder.tm_mc.text = currentItem.deliveryTime
        holder.feu1.text = currentItem.category1
        holder.feu2.text = currentItem.category2
        holder.feu3.text = currentItem.category3

        // Handle "Add to Crate" button click
        holder.addToCart.setOnClickListener {
            listener.onAddToCrateClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return typList.size
    }

    interface RecyclerViewClickListener {
        fun onClick(v: View?, position: Int)
        fun onAddToCrateClicked(position: Int)
    }

    fun View.setOnClickListenerWithPosition(clickListener: (position: Int) -> Unit) {
        this.setOnClickListener {
            clickListener.invoke(it.tag as Int)
        }
    }
    fun getItem(position: Int): typ {
        return typList[position]
    }
}
