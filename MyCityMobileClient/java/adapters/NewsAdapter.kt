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
import com.example.communityappmobile.models.news.News
import com.example.communityappmobile.models.viewHolders.FacilityViewHolder
import com.example.communityappmobile.models.viewHolders.NewsViewHolder
import com.example.communityappmobile.services.ui.facilities.FacilityDetailsFragment
import com.example.communityappmobile.services.ui.news.NewsDetailsFragment
import java.util.*

@SuppressLint("NotifyDataSetChanged")
class NewsAdapter(
    private val context: Context,
    private var dataSource: ArrayList<News>,
    private val navController: NavController
) : RecyclerView.Adapter<NewsViewHolder>(){


    var newsList = ArrayList<News>()
    val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    init {
        newsList = dataSource
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = inflater.inflate(R.layout.list_item_news, parent, false)
        return NewsViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bindData(newsList.get(position))



        holder.itemView.setOnClickListener {
            val news = newsList[position]
            Log.e("", news.toString())

            val bundle = Bundle().apply {
                putSerializable("news", news)
            }

            val newsDetailsFragment = NewsDetailsFragment()
            newsDetailsFragment.arguments = bundle

            navController.navigate(R.id.navigation_news_details, bundle)
        }

    }


    override fun getItemCount(): Int {
        return newsList.size
    }


    fun addAll(news: ArrayList<News>) {
        newsList.addAll(news)
        notifyDataSetChanged()
    }

}

