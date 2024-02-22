package com.example.communityappmobile.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.communityappmobile.R
import com.example.communityappmobile.models.alerts.Alert
import com.example.communityappmobile.models.news.News
import com.example.communityappmobile.models.viewHolders.AlertViewHolder
import com.example.communityappmobile.models.viewHolders.NewsViewHolder
import com.example.communityappmobile.services.ui.news.NewsDetailsFragment
import java.util.ArrayList

@SuppressLint("NotifyDataSetChanged")
class AlertsAdapter(
    private val context: Context,
    private var dataSource: ArrayList<Alert>,
) : RecyclerView.Adapter<AlertViewHolder>(){


    var alertsList = ArrayList<Alert>()
    val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    init {
        alertsList = dataSource
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val view = inflater.inflate(R.layout.list_item_alert, parent, false)
        return AlertViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        holder.bindData(alertsList.get(position))

    }


    override fun getItemCount(): Int {
        return alertsList.size
    }


    fun addAll(alert: ArrayList<Alert>) {
        alertsList.addAll(alert)
        notifyDataSetChanged()
    }

}

