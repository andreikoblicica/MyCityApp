package com.example.communityappmobile.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.communityappmobile.R
import com.example.communityappmobile.models.events.Event
import com.example.communityappmobile.models.viewHolders.EventViewHolder
import com.example.communityappmobile.models.viewHolders.FacilityViewHolder
import com.example.communityappmobile.services.ui.events.EventDetailsFragment
import com.example.communityappmobile.services.ui.facilities.FacilityDetailsFragment
import java.util.*

@SuppressLint("NotifyDataSetChanged")
class EventsAdapter(
    private val context: Context,
    private var dataSource: ArrayList<Event>,
    private val navController: NavController
) : RecyclerView.Adapter<EventViewHolder>(),Filterable {


    var eventsList = ArrayList<Event>()
    val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    init {
        eventsList = dataSource
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = inflater.inflate(R.layout.list_item_event, parent, false)
        return EventViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bindData(eventsList.get(position))



        holder.itemView.setOnClickListener {
            val event = eventsList[position]


            val bundle = Bundle().apply {
                putSerializable("event", event)
                putSerializable("user_event",false)
            }

            val eventDetailsFragment = EventDetailsFragment()
            eventDetailsFragment.arguments = bundle

            navController.navigate(R.id.navigation_event_details, bundle)
        }

    }


    override fun getItemCount(): Int {
        return eventsList.size
    }

    fun addAll(events: ArrayList<Event>) {
        eventsList.addAll(events)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()||charSearch=="All") {
                    eventsList = dataSource
                } else {
                    val resultList = ArrayList<Event>()
                    for (row in dataSource) {
                        if (row.type.lowercase(Locale.ROOT) == charSearch.lowercase(Locale.ROOT)
                        ) {
                            resultList.add(row)
                        }
                    }
                    eventsList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = eventsList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                eventsList = results?.values as ArrayList<Event>
                notifyDataSetChanged()
            }
        }
    }
}
