package com.example.communityappmobile.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.communityappmobile.R
import com.example.communityappmobile.models.elements.MainMenuOption
import com.example.communityappmobile.models.institutions.Institution
import com.example.communityappmobile.models.issue.InvolvedInstitution
import com.example.communityappmobile.models.viewHolders.InstitutionsViewHolder
import com.example.communityappmobile.models.viewHolders.InvolvedInstitutionViewHolder
import com.example.communityappmobile.services.ui.institutions.InstitutionDetailsFragment
import java.util.ArrayList

@SuppressLint("NotifyDataSetChanged")
class InvolvedInstitutionsAdapter(
    private val context: Context,
    private var dataSource: ArrayList<InvolvedInstitution>,
) : RecyclerView.Adapter<InvolvedInstitutionViewHolder>() {


    var institutionsList = ArrayList<InvolvedInstitution>()
    val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    init {
        institutionsList = dataSource
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvolvedInstitutionViewHolder {
        val view = inflater.inflate(R.layout.list_item_involved_institution, parent, false)
        return InvolvedInstitutionViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: InvolvedInstitutionViewHolder, position: Int) {
        holder.bindData(institutionsList.get(position))

    }


    override fun getItemCount(): Int {
        return institutionsList.size
    }



    fun addAll(institutions: ArrayList<InvolvedInstitution>) {
        institutionsList.addAll(institutions)
        notifyDataSetChanged()
    }

}


