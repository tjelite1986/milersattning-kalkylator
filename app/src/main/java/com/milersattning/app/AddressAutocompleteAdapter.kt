package com.milersattning.app

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import java.util.concurrent.ConcurrentHashMap

class AddressAutocompleteAdapter(
    private val context: Context
) : BaseAdapter(), Filterable {
    
    private val autocompleteService = AddressAutocompleteService()
    private var suggestions = mutableListOf<AddressSuggestion>()
    private val mainHandler = Handler(Looper.getMainLooper())
    private val pendingQueries = ConcurrentHashMap<String, Boolean>()
    
    companion object {
        private const val TAG = "AddressAdapter"
    }
    
    override fun getCount(): Int = suggestions.size
    
    override fun getItem(position: Int): AddressSuggestion = suggestions[position]
    
    override fun getItemId(position: Int): Long = position.toLong()
    
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            android.R.layout.simple_dropdown_item_1line, 
            parent, 
            false
        )
        
        val suggestion = getItem(position)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = suggestion.displayName
        
        return view
    }
    
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                // Vi returnerar alltid tomma resultat här eftersom vi hanterar allt asynkront
                return FilterResults().apply {
                    values = emptyList<AddressSuggestion>()
                    count = 0
                }
            }
            
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (constraint == null || constraint.length < 2) {
                    suggestions.clear()
                    notifyDataSetChanged()
                    return
                }
                
                val query = constraint.toString()
                
                // Avbryt tidigare queries
                pendingQueries.keys.forEach { pendingQueries[it] = false }
                pendingQueries[query] = true
                
                // Sök asynkront
                autocompleteService.searchAddresses(query) { addressSuggestions ->
                    // Kontrollera om denna query fortfarande är relevant
                    if (pendingQueries[query] == true) {
                        mainHandler.post {
                            suggestions.clear()
                            suggestions.addAll(addressSuggestions)
                            notifyDataSetChanged()
                            
                            Log.d(TAG, "Updated suggestions for '$query': ${suggestions.size} items")
                        }
                    }
                    
                    // Ta bort från pending queries
                    pendingQueries.remove(query)
                }
            }
            
            override fun convertResultToString(resultValue: Any?): CharSequence {
                return if (resultValue is AddressSuggestion) {
                    resultValue.displayName
                } else {
                    resultValue?.toString() ?: ""
                }
            }
        }
    }
    
    fun getSelectedAddress(position: Int): String {
        return if (position >= 0 && position < suggestions.size) {
            suggestions[position].displayName
        } else {
            ""
        }
    }
    
    fun getSelectedShortName(position: Int): String {
        return if (position >= 0 && position < suggestions.size) {
            suggestions[position].shortName
        } else {
            ""
        }
    }
}