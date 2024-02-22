package com.example.communityappmobile.models.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.communityappmobile.R
import com.example.communityappmobile.adapters.FacilitiesAdapter
import com.example.communityappmobile.models.elements.Facility


class FacilityViewHolder(private val view: View, private val adapter: FacilitiesAdapter)  :
    RecyclerView.ViewHolder(view) {
    private var name: TextView
    private var type: TextView
    private var location: TextView
    private var image: ImageView

    init {
        name = view.findViewById(R.id.facility_name)
        type=view.findViewById(R.id.facility_type)
        location=view.findViewById(R.id.facility_location)
        image=view.findViewById(R.id.facility_image)
    }

    fun bindData(facility: Facility) {
        name.text = facility.name
        type.text=facility.type.uppercase()
        location.text=facility.location
        Glide.with(view).load(facility.imageUrl).into(image)
    }






}