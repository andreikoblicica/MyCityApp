package com.example.communityappmobile.services.ui.facilities


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.communityappmobile.adapters.FacilitiesAdapter
import com.example.communityappmobile.apis.ServerAPI
import com.example.communityappmobile.databinding.FragmentFacilitiesBinding
import com.example.communityappmobile.models.elements.Facility
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FacilitiesFragment : Fragment(), AdapterView.OnItemSelectedListener, OnItemClickListener {

    private var _binding: FragmentFacilitiesBinding? = null

    private val binding get() = _binding!!

    lateinit var facilitiesAdapter: FacilitiesAdapter

    private lateinit var facilityTypeTextView: TextInputLayout

    private lateinit var facilitiesViewModel:FacilitiesViewModel


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)


        facilityTypeTextView=binding.facilityTypesTextView
        val eventTypeAdapter = ArrayAdapter(requireContext(), R.layout.menu_list_item, facilitiesViewModel.getFacilityTypes())
        (facilityTypeTextView.editText as AutoCompleteTextView).setAdapter(eventTypeAdapter)

        (facilityTypeTextView.editText as AutoCompleteTextView).onItemClickListener=this
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        facilitiesViewModel =
            ViewModelProvider(this).get(FacilitiesViewModel::class.java)

        _binding = FragmentFacilitiesBinding.inflate(inflater, container, false)


        facilityTypeTextView=binding.facilityTypesTextView
        val adapter = ArrayAdapter(requireContext(), R.layout.menu_list_item, facilitiesViewModel.getFacilityTypes())
        (facilityTypeTextView.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        (facilityTypeTextView.editText as? AutoCompleteTextView)?.onItemClickListener=this


            val facilitiesRecyclerView: RecyclerView = binding.facilitiesRecyclerView
            val emptyList: java.util.ArrayList<Facility> = arrayListOf()

            facilitiesAdapter = FacilitiesAdapter(requireContext(),emptyList,findNavController())
            facilitiesRecyclerView.adapter = facilitiesAdapter
            facilitiesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            var facilities: ArrayList<Facility>
            val serverAPI= ServerAPI.createApi()
            serverAPI.getAllFacilities().enqueue(object:
                Callback<ArrayList<Facility>> {
                override fun onResponse(
                    call: Call<ArrayList<Facility>>,
                    response: Response<ArrayList<Facility>>
                ) {
                    facilities=response.body() as java.util.ArrayList<Facility>
                    facilitiesAdapter.addAll(facilities)
                    facilitiesAdapter.filter.filter(facilityTypeTextView.editText?.text as CharSequence?)
                    facilitiesViewModel.setFacilities(facilities)
                }

                override fun onFailure(call: Call<ArrayList<Facility>>, t: Throwable) {
                    Log.e("GET_FAIL",t.toString())
                }

            })

            return binding.root
        }

                override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val selectedType = p0?.getItemAtPosition(p2)
        facilitiesAdapter.filter.filter(selectedType as CharSequence?)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val selectedType = p0?.getItemAtPosition(p2)
        facilitiesAdapter.filter.filter(selectedType as CharSequence?)
    }


}