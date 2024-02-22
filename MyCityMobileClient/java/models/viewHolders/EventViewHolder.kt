package com.example.communityappmobile.models.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.communityappmobile.R
import com.example.communityappmobile.adapters.EventsAdapter
import com.example.communityappmobile.models.events.Event

class EventViewHolder(private val view: View, private val adapter: EventsAdapter)  :
    RecyclerView.ViewHolder(view) {
    private var title: TextView
    private var type: TextView
    private var location: TextView
    private var date: TextView
    private var interestedCount: TextView
    private var image: ImageView

    init {
        title = view.findViewById(R.id.event_title)
        type=view.findViewById(R.id.event_type)
        location=view.findViewById(R.id.event_location)
        date=view.findViewById(R.id.event_date)
        interestedCount=view.findViewById(R.id.event_interested_count)
        image=view.findViewById(R.id.event_image)
    }

    fun bindData(event: Event) {
        title.text = event.title
        type.text=event.type.uppercase()
        location.text=event.location
        interestedCount.text=event.favoriteCount.toString()+" interested"
        var endDate=""
        if(event.endDateTime!=""){
            endDate=" - "+event.endDateTime
        }
        date.text=event.startDateTime+endDate
        Glide.with(view).load(event.image).into(image)
    }






}