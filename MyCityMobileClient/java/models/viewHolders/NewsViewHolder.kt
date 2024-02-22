package com.example.communityappmobile.models.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.communityappmobile.R
import com.example.communityappmobile.adapters.NewsAdapter
import com.example.communityappmobile.models.news.News

class NewsViewHolder(private val view: View, private val adapter: NewsAdapter)  :
    RecyclerView.ViewHolder(view) {
    private var title: TextView
    private var date: TextView
    private var institution: TextView
    private var image: ImageView

    init {
        title = view.findViewById(R.id.news_title)
        date=view.findViewById(R.id.news_date)
        institution=view.findViewById(R.id.news_institution)
        image=view.findViewById(R.id.news_image)
    }

    fun bindData(news: News) {
        title.text = news.title
        date.text=news.dateTime
        if(news.institution!=""){
            institution.text=news.institution
        }else{
            institution.text="Community App"
        }

        Glide.with(view).load(news.image).into(image)
    }






}