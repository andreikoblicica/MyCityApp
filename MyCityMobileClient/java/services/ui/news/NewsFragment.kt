package com.example.communityappmobile.services.ui.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.communityappmobile.adapters.NewsAdapter
import com.example.communityappmobile.apis.ServerAPI
import com.example.communityappmobile.databinding.FragmentNewsBinding
import com.example.communityappmobile.models.news.News
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null

    private val binding get() = _binding!!

    lateinit var newsAdapter: NewsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNewsBinding.inflate(inflater, container, false)


        val newsRecyclerView: RecyclerView = binding.newsRecyclerView
        val emptyList: java.util.ArrayList<News> = arrayListOf()

        newsAdapter = NewsAdapter(requireContext(), emptyList, findNavController())
        newsRecyclerView.adapter = newsAdapter
        newsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        var news: ArrayList<News>
        val serverAPI = ServerAPI.createApi()
        serverAPI.getNews().enqueue(object :
            Callback<ArrayList<News>> {
            override fun onResponse(
                call: Call<ArrayList<News>>,
                response: Response<ArrayList<News>>
            ) {
                news = response.body() as java.util.ArrayList<News>
                newsAdapter.addAll(news.reversed() as java.util.ArrayList<News>)
            }

            override fun onFailure(call: Call<ArrayList<News>>, t: Throwable) {
                Log.e("GET_FAIL", t.toString())
            }

        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}