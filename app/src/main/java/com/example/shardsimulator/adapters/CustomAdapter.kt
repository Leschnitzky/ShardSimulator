package com.example.shardsimulator.adapters

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shardsimulator.R
import com.example.shardsimulator.models.DrawCard


class CustomAdapter(private val dataSet: List<DrawCard>, private val context: Context) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
        val imageView: ImageView = view.findViewById(R.id.imageView)

        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.shard_pull_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val drawCard = dataSet.get(position)
        Glide
            .with(context)
            .load(drawCard.imageUrl)
            .placeholder(R.drawable.sac_shard)
            .into(viewHolder.imageView)
        val color = rarityToColor(drawCard.rarity)
        viewHolder.imageView.setBackgroundColor(Color.parseColor(context.resources.getString(color)))
        viewHolder.textView.setText(drawCard.name)
    }

    private fun rarityToColor(rarity: String): Int {
        when(rarity) {
            "legendary" -> return R.color.md_yellow_900
            "epic" -> return R.color.md_purple_600
            "rare" -> return R.color.md_blue_700
        }
        return -1
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
