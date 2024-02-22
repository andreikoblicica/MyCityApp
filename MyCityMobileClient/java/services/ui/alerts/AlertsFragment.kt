package com.example.communityappmobile.services.ui.alerts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.communityappmobile.adapters.AlertsAdapter
import com.example.communityappmobile.apis.ServerAPI
import com.example.communityappmobile.databinding.FragmentAlertsBinding
import com.example.communityappmobile.models.alerts.Alert
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlertsFragment : Fragment(){

    private var _binding: FragmentAlertsBinding? = null

    private val binding get() = _binding!!

    lateinit var alertsAdapter: AlertsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentAlertsBinding.inflate(inflater, container, false)

        val alertsRecyclerView: RecyclerView = binding.alertsRecyclerView
        val emptyList: java.util.ArrayList<Alert> = arrayListOf()

        alertsAdapter = AlertsAdapter(requireContext(),emptyList)
        alertsRecyclerView.adapter = alertsAdapter
        alertsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        var alerts: ArrayList<Alert>
        val serverAPI= ServerAPI.createApi()
        serverAPI.getAllAlerts().enqueue(object:
            Callback<ArrayList<Alert>> {
            override fun onResponse(
                call: Call<ArrayList<Alert>>,
                response: Response<ArrayList<Alert>>
            ) {
                alerts=response.body() as java.util.ArrayList<Alert>
                alertsAdapter.addAll(alerts)
            }

            override fun onFailure(call: Call<ArrayList<Alert>>, t: Throwable) {
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