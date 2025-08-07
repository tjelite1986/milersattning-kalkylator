package com.milersattning.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class FavoritesAdapter(
    private val favorites: List<FavoritePlace>,
    private val onFavoriteClick: (FavoritePlace) -> Unit,
    private val onOptionsClick: (FavoritePlace) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.favorite_name)
        val addressText: TextView = view.findViewById(R.id.favorite_address)
        val dateText: TextView = view.findViewById(R.id.favorite_date)
        val optionsButton: ImageView = view.findViewById(R.id.favorite_options)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dialog_favorite_item, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favorite = favorites[position]
        
        holder.nameText.text = favorite.name
        holder.addressText.text = favorite.displayName
        holder.dateText.text = "Tillagd: ${favorite.dateAdded}"
        
        holder.itemView.setOnClickListener {
            onFavoriteClick(favorite)
        }
        
        holder.optionsButton.setOnClickListener {
            onOptionsClick(favorite)
        }
    }

    override fun getItemCount() = favorites.size
}