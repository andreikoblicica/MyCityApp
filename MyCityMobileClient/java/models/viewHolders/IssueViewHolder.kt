package com.example.communityappmobile.models.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.communityappmobile.R
import com.example.communityappmobile.adapters.IssuesAdapter
import com.example.communityappmobile.models.issue.Issue

class IssueViewHolder(private val view: View, private val adapter: IssuesAdapter)  :
    RecyclerView.ViewHolder(view) {
    private var title: TextView
    private var type: TextView
    private var date: TextView
    private var status: TextView

    init {
        title = view.findViewById(R.id.list_issue_title)
        type=view.findViewById(R.id.list_issue_type)
        date=view.findViewById(R.id.list_issue_date)
        status=view.findViewById(R.id.list_issue_status)
    }

    fun bindData(issue: Issue) {
        title.text = issue.title
        type.text=issue.type.uppercase()
        date.text=issue.dateTime
        status.text=issue.status
    }






}