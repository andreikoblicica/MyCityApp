package com.example.communityappmobile.services.ui.events

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.communityappmobile.adapters.EventsAdapter
import com.example.communityappmobile.apis.ServerAPI
import com.example.communityappmobile.databinding.FragmentFavoriteEventsBinding
import com.example.communityappmobile.models.auth.User
import com.example.communityappmobile.models.events.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteEventsFragment : Fragment() {

    private var _binding: FragmentFavoriteEventsBinding? = null

    private val binding get() = _binding!!

    lateinit var eventsAdapter: EventsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoriteEventsBinding.inflate(inflater, container, false)

        val favoriteEventsRecyclerView: RecyclerView = binding.favoriteEventsRecyclerView
        val emptyList: java.util.ArrayList<Event> = arrayListOf()

        eventsAdapter = EventsAdapter(requireContext(),emptyList,findNavController())
        favoriteEventsRecyclerView.adapter = eventsAdapter
        favoriteEventsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        var favoriteEvents: ArrayList<Event>
        val serverAPI= ServerAPI.createApi()
        serverAPI.getFavoriteEventsByUser(User.id).enqueue(object:
            Callback<ArrayList<Event>> {
            override fun onResponse(
                call: Call<ArrayList<Event>>,
                response: Response<ArrayList<Event>>
            ) {
                favoriteEvents=response.body() as java.util.ArrayList<Event>
                eventsAdapter.addAll(favoriteEvents.reversed() as java.util.ArrayList<Event>)
            }

            override fun onFailure(call: Call<ArrayList<Event>>, t: Throwable) {
                Log.e("GET_FAIL",t.toString())
            }

        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}