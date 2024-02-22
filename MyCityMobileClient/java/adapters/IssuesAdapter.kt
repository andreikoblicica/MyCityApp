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
import com.example.communityappmobile.models.elements.Facility
import com.example.communityappmobile.models.issue.Issue
import com.example.communityappmobile.models.viewHolders.FacilityViewHolder
import com.example.communityappmobile.models.viewHolders.IssueViewHolder
import com.example.communityappmobile.services.ui.facilities.FacilityDetailsFragment
import com.example.communityappmobile.services.ui.issues.IssueDetailsFragment
import java.util.*

@SuppressLint("NotifyDataSetChanged")
class IssuesAdapter(
    private val context: Context,
    private var dataSource: ArrayList<Issue>,
    private val navController: NavController
) : RecyclerView.Adapter<IssueViewHolder>() {


    var issuesList = ArrayList<Issue>()
    val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    init {
        issuesList = dataSource
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueViewHolder {
        val view = inflater.inflate(R.layout.list_item_issue, parent, false)
        return IssueViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: IssueViewHolder, position: Int) {
        holder.bindData(issuesList.get(position))



        holder.itemView.setOnClickListener {
            val issue = issuesList[position]

            val bundle = Bundle().apply {
                putSerializable("issue", issue)
            }

            val issueDetailsFragment = IssueDetailsFragment()
            issueDetailsFragment.arguments = bundle

            navController.navigate(R.id.navigation_issue_details, bundle)
        }

    }


    override fun getItemCount(): Int {
        return issuesList.size
    }


    fun addAll(issues: ArrayList<Issue>) {
        issuesList.addAll(issues)
        notifyDataSetChanged()
    }

}
