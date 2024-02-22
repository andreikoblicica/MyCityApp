package com.example.communityappmobile.models.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.communityappmobile.R
import com.example.communityappmobile.adapters.InvolvedInstitutionsAdapter
import com.example.communityappmobile.models.issue.InvolvedInstitution

class InvolvedInstitutionViewHolder(private val view: View, private val adapter: InvolvedInstitutionsAdapter)  :
    RecyclerView.ViewHolder(view) {
    private var name: TextView
    private var status: TextView

    init {
        name = view.findViewById(R.id.involved_institution_name)
        status=view.findViewById(R.id.involved_institution_status)
    }

    fun bindData(institution: InvolvedInstitution) {
        name.text = institution.institutionName
        status.text=institution.issueStatus
    }






}