package com.example.communityappmobile.models.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.communityappmobile.R
import com.example.communityappmobile.adapters.InstitutionsAdapter
import com.example.communityappmobile.models.institutions.Institution

class InstitutionsViewHolder(private val view: View, private val adapter: InstitutionsAdapter)  :
    RecyclerView.ViewHolder(view) {
    private var name: TextView
    private var address: TextView
    private var image: ImageView

    init {
        name = view.findViewById(R.id.institution_name)
        address=view.findViewById(R.id.institution_address)
        image=view.findViewById(R.id.institution_image)
    }

    fun bindData(institution: Institution) {
        name.text = institution.name
        address.text=institution.address
        Glide.with(view).load(institution.image).into(image)
    }






}