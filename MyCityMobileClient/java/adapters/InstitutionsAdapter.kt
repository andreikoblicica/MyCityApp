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
import com.example.communityappmobile.models.institutions.Institution
import com.example.communityappmobile.models.viewHolders.FacilityViewHolder
import com.example.communityappmobile.models.viewHolders.InstitutionsViewHolder
import com.example.communityappmobile.services.ui.facilities.FacilityDetailsFragment
import com.example.communityappmobile.services.ui.institutions.InstitutionDetailsFragment
import java.util.*

@SuppressLint("NotifyDataSetChanged")
class InstitutionsAdapter(
    private val context: Context,
    private var dataSource: ArrayList<Institution>,
    private val navController: NavController
) : RecyclerView.Adapter<InstitutionsViewHolder>() {


    var institutionsList = ArrayList<Institution>()
    val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    init {
        institutionsList = dataSource
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstitutionsViewHolder {
        val view = inflater.inflate(R.layout.list_item_institution, parent, false)
        return InstitutionsViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: InstitutionsViewHolder, position: Int) {
        holder.bindData(institutionsList.get(position))



        holder.itemView.setOnClickListener {
            val institution = institutionsList[position]

            val bundle = Bundle().apply {
                putSerializable("institution", institution)
            }

            val institutionDetailsFragment = InstitutionDetailsFragment()
            institutionDetailsFragment.arguments = bundle

            navController.navigate(R.id.navigation_institution_details, bundle)
        }

    }


    override fun getItemCount(): Int {
        return institutionsList.size
    }


    fun addAll(facilities: ArrayList<Institution>) {
        institutionsList.addAll(facilities)
        notifyDataSetChanged()
    }

}


