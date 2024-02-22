package com.example.communityappmobile.services.ui.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.communityappmobile.databinding.FragmentNewsDetailsBinding
import com.example.communityappmobile.models.news.News



class NewsDetailsFragment : Fragment(),OnClickListener {

    private var _binding: FragmentNewsDetailsBinding? = null

    private val binding get() = _binding!!

    private lateinit var news:News

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        news = (arguments?.getSerializable("news") as? News)!!
        _binding = FragmentNewsDetailsBinding.inflate(inflater, container, false)

        val imageView=binding.newsDetailsImage
        Glide.with(this).load(news!!.image).into(imageView)

        binding.newsDetailsTitle.text= news.title
        binding.newsDetailsDate.text=news.dateTime
        binding.newsDetailsInstitution.text=news.institution
        binding.newsDetailsDescription.text=news.description

        val buttonView=binding.viewNews
        buttonView.setOnClickListener(this)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(p0: View?) {
        val uri= Uri.parse(news.websiteLink)
        val openLinkIntent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(openLinkIntent)
    }



}