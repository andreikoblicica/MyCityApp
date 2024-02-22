package com.example.communityappmobile.services.ui.events

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.OnClickListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.communityappmobile.R
import com.example.communityappmobile.adapters.MyEventsAdapter
import com.example.communityappmobile.apis.ServerAPI

import com.example.communityappmobile.databinding.FragmentMyEventsBinding
import com.example.communityappmobile.models.auth.User

import com.example.communityappmobile.models.events.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyEventsFragment : Fragment(),OnClickListener {

    private var _binding: FragmentMyEventsBinding? = null

    private val binding get() = _binding!!

    lateinit var eventsAdapter: MyEventsAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val eventsViewModel =
            ViewModelProvider(this).get(EventsViewModel::class.java)

        _binding = FragmentMyEventsBinding.inflate(inflater, container, false)

        val createEventButton=binding.createEventButton
        createEventButton.setOnClickListener(this)

        val eventsRecyclerView: RecyclerView = binding.myeventsRecyclerView
        val emptyList: java.util.ArrayList<Event> = arrayListOf()

        eventsAdapter = MyEventsAdapter(requireContext(),emptyList,findNavController())
        eventsRecyclerView.adapter = eventsAdapter
        eventsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        var events: ArrayList<Event>
        val serverAPI= ServerAPI.createApi()
        serverAPI.getEventsByUser(User.id).enqueue(object:
            Callback<ArrayList<Event>> {
            override fun onResponse(
                call: Call<ArrayList<Event>>,
                response: Response<ArrayList<Event>>
            ) {
                events=response.body() as java.util.ArrayList<Event>
                eventsAdapter.addAll(events.reversed() as java.util.ArrayList<Event>)
                eventsViewModel.setEvents(events.reversed() as java.util.ArrayList<Event>)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.my_events_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.go_to_favorites){
            findNavController().navigate(R.id.navigation_favorite_events)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(p0: View?) {
        findNavController().navigate(R.id.navigation_create_event)
    }


}