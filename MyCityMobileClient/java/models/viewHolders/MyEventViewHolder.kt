package com.example.communityappmobile.models.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.communityappmobile.R
import com.example.communityappmobile.adapters.MyEventsAdapter
import com.example.communityappmobile.models.events.Event

class MyEventViewHolder(private val view: View, private val adapter: MyEventsAdapter)  :
    RecyclerView.ViewHolder(view) {
    private var title: TextView
    private var type: TextView
    private var location: TextView
    private var date: TextView
    private var image: ImageView
    private var status: TextView
    private var interestedCount:TextView

    init {
        title = view.findViewById(R.id.my_event_title)
        type=view.findViewById(R.id.my_event_type)
        location=view.findViewById(R.id.my_event_location)
        date=view.findViewById(R.id.my_event_date)
        image=view.findViewById(R.id.my_event_image)
        status=view.findViewById(R.id.my_event_status)
        interestedCount=view.findViewById(R.id.my_event_interested_count)
    }

    fun bindData(event: Event) {
        title.text = event.title
        type.text=event.type.uppercase()
        location.text=event.location
        var endDate=""
        if(event.endDateTime!=""){
            endDate=" - "+event.endDateTime
        }
        date.text=event.startDateTime+endDate
        Glide.with(view).load(event.image).into(image)
        status.text=event.status
        interestedCount.text=event.favoriteCount.toString()+" interested"
    }






}