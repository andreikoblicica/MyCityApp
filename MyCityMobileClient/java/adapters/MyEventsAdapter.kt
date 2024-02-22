package com.example.communityappmobile.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.communityappmobile.R
import com.example.communityappmobile.models.events.Event
import com.example.communityappmobile.models.viewHolders.EventViewHolder
import com.example.communityappmobile.models.viewHolders.MyEventViewHolder
import com.example.communityappmobile.services.ui.events.EventDetailsFragment
import java.util.ArrayList

@SuppressLint("NotifyDataSetChanged")
class MyEventsAdapter(
    private val context: Context,
    private var dataSource: ArrayList<Event>,
    private val navController: NavController
) : RecyclerView.Adapter<MyEventViewHolder>() {


    var eventsList = ArrayList<Event>()
    val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    init {
        eventsList = dataSource
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyEventViewHolder {
        val view = inflater.inflate(R.layout.list_item_my_event, parent, false)
        return MyEventViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: MyEventViewHolder, position: Int) {
        holder.bindData(eventsList.get(position))



        holder.itemView.setOnClickListener {
            val event = eventsList[position]


            val bundle = Bundle().apply {
                putSerializable("event", event)
                putSerializable("user_event",true)
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
}
