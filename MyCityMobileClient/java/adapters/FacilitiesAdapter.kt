package com.example.communityappmobile.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.NavController

import androidx.recyclerview.widget.RecyclerView
import com.example.communityappmobile.R
import com.example.communityappmobile.models.elements.Facility
import com.example.communityappmobile.models.viewHolders.FacilityViewHolder
import com.example.communityappmobile.services.ui.facilities.FacilitiesViewModel
import com.example.communityappmobile.services.ui.facilities.FacilityDetailsFragment
import java.util.*

@SuppressLint("NotifyDataSetChanged")
class FacilitiesAdapter(
     private val context: Context,
    private var dataSource: ArrayList<Facility>,
     private val navController: NavController
) : RecyclerView.Adapter<FacilityViewHolder>(), Filterable {


    var facilitiesList = ArrayList<Facility>()
    val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    init {
        facilitiesList = dataSource
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacilityViewHolder {
        val view = inflater.inflate(R.layout.list_item_facility, parent, false)
        return FacilityViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: FacilityViewHolder, position: Int) {
        holder.bindData(facilitiesList.get(position))

        holder.itemView.setOnClickListener {
            val facility = facilitiesList[position]
            Log.e("", facility.toString())

            val bundle = Bundle().apply {
                putSerializable("facility", facility)
            }

            val facilityDetailsFragment = FacilityDetailsFragment()
            facilityDetailsFragment.arguments = bundle

            navController.navigate(R.id.navigation_facility_details, bundle)
        }

    }


    override fun getItemCount(): Int {
        return facilitiesList.size
    }


    fun addAll(facilities: ArrayList<Facility>) {
        facilitiesList.addAll(facilities)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()||charSearch=="All") {
                    facilitiesList = dataSource
                } else {
                    val resultList = ArrayList<Facility>()
                    for (row in dataSource) {
                        if (row.type.lowercase(Locale.ROOT) == charSearch.lowercase(Locale.ROOT)
                        ) {
                            resultList.add(row)
                        }
                    }
                    facilitiesList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = facilitiesList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                facilitiesList = results?.values as ArrayList<Facility>
                notifyDataSetChanged()
            }
        }
    }
}





