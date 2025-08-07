package com.milersattning.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TripsAdapter(
    private val trips: List<Trip>,
    private val onTripClick: (Trip) -> Unit,
    private val onTripLongClick: ((Trip) -> Unit)? = null
) : RecyclerView.Adapter<TripsAdapter.TripViewHolder>() {

    class TripViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val descriptionText: TextView = view.findViewById(R.id.trip_description)
        val amountText: TextView = view.findViewById(R.id.trip_amount)
        val distanceText: TextView = view.findViewById(R.id.trip_distance)
        val purposeText: TextView = view.findViewById(R.id.trip_purpose)
        val timeText: TextView = view.findViewById(R.id.trip_time)
        val dateText: TextView = view.findViewById(R.id.trip_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dialog_trip_item, parent, false)
        return TripViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = trips[position]
        
        holder.descriptionText.text = trip.description
        holder.amountText.text = String.format("%.2f SEK", trip.amount)
        holder.distanceText.text = String.format("%.1f km", trip.distance)
        holder.purposeText.text = "${trip.purpose} (${trip.vehicleType})"
        
        // Formatera tid
        val timeInfo = when {
            trip.startTime.isNotEmpty() && trip.endTime.isNotEmpty() -> 
                "${trip.startTime} - ${trip.endTime}"
            trip.startTime.isNotEmpty() -> 
                "Start: ${trip.startTime}"
            else -> 
                "Ingen tid angiven"
        }
        holder.timeText.text = timeInfo
        
        holder.dateText.text = trip.date
        
        holder.itemView.setOnClickListener {
            onTripClick(trip)
        }
        
        holder.itemView.setOnLongClickListener {
            onTripLongClick?.invoke(trip)
            true
        }
    }

    override fun getItemCount() = trips.size
}