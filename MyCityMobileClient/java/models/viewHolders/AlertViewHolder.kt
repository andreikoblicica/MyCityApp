package com.example.communityappmobile.models.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.communityappmobile.R
import com.example.communityappmobile.adapters.AlertsAdapter
import com.example.communityappmobile.models.alerts.Alert


class AlertViewHolder(private val view: View, private val adapter: AlertsAdapter)  :
    RecyclerView.ViewHolder(view) {
    private var title: TextView
    private var dateTime: TextView
    private var institution: TextView
    private var description: TextView

    init {
        title = view.findViewById(R.id.alert_title)
        dateTime=view.findViewById(R.id.alert_date_time)
        institution=view.findViewById(R.id.alert_institution)
        description=view.findViewById(R.id.alert_description)
    }

    fun bindData(alert: Alert) {
        title.text = alert.title
        dateTime.text=alert.dateTime
        if(alert.institution!=""){
            institution.text=alert.institution
        }else{
            institution.text="MyCity App"
        }

        description.text=alert.description
    }






}