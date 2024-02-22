package com.example.communityappmobile.services.ui.events


import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.communityappmobile.R
import com.example.communityappmobile.adapters.EventsAdapter
import com.example.communityappmobile.apis.ServerAPI
import com.example.communityappmobile.databinding.FragmentEventsBinding
import com.example.communityappmobile.models.events.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventsFragment : Fragment(),OnItemClickListener {

    private var _binding: FragmentEventsBinding? = null

    private val binding get() = _binding!!


    private lateinit var eventsAdapter: EventsAdapter

    private lateinit var eventTypesInput: AutoCompleteTextView

    private lateinit var eventsViewModel: EventsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        eventTypesInput=binding.eventTypesField
        val eventTypeAdapter = ArrayAdapter(requireContext(), R.layout.menu_list_item, eventsViewModel.getEventTypes())
        eventTypesInput.setAdapter(eventTypeAdapter)
        eventTypesInput.onItemClickListener=this

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        eventsViewModel = ViewModelProvider(this).get(EventsViewModel::class.java)

        _binding = FragmentEventsBinding.inflate(inflater, container, false)

        eventTypesInput=binding.eventTypesField
        val eventTypeAdapter = ArrayAdapter(requireContext(), R.layout.menu_list_item, eventsViewModel.getEventTypes())
        eventTypesInput.setAdapter(eventTypeAdapter)

        eventTypesInput.onItemClickListener=this

        val eventsRecyclerView: RecyclerView = binding.eventsRecyclerView
        val emptyList: java.util.ArrayList<Event> = arrayListOf()

        eventsAdapter = EventsAdapter(requireContext(),emptyList,findNavController())
        eventsRecyclerView.adapter = eventsAdapter
        eventsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        var events: ArrayList<Event>
        val serverAPI= ServerAPI.createApi()
        serverAPI.getEventsByStatus("Approved").enqueue(object:
            Callback<ArrayList<Event>> {
            override fun onResponse(
                call: Call<ArrayList<Event>>,
                response: Response<ArrayList<Event>>
            ) {
                events=response.body() as java.util.ArrayList<Event>
                eventsAdapter.addAll(events.reversed() as java.util.ArrayList<Event>)
                eventsAdapter.filter.filter(eventTypesInput.text as CharSequence?)
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
        inflater.inflate ( R.menu.events_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.go_to_my_events){
            findNavController().navigate(R.id.navigation_my_events)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val selectedType = p0?.getItemAtPosition(p2)
        Log.e("type",selectedType.toString())
        eventsAdapter.filter.filter(selectedType as CharSequence?)
    }


}