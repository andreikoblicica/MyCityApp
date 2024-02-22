package com.example.communityappmobile.services.ui.institutions


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
import com.example.communityappmobile.adapters.InstitutionsAdapter
import com.example.communityappmobile.apis.ServerAPI
import com.example.communityappmobile.databinding.FragmentInstitutionsBinding
import com.example.communityappmobile.models.institutions.Institution
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InstitutionsFragment : Fragment(){

    private var _binding: FragmentInstitutionsBinding? = null

    private val binding get() = _binding!!

    lateinit var institutionsAdapter: InstitutionsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentInstitutionsBinding.inflate(inflater, container, false)

        val institutionsRecyclerView: RecyclerView = binding.institutionsRecyclerView
        val emptyList: java.util.ArrayList<Institution> = arrayListOf()

        institutionsAdapter = InstitutionsAdapter(requireContext(),emptyList,findNavController())
        institutionsRecyclerView.adapter = institutionsAdapter
        institutionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        var institutions: ArrayList<Institution>
        val serverAPI= ServerAPI.createApi()
        serverAPI.getAllInstitutions().enqueue(object:
            Callback<ArrayList<Institution>> {
            override fun onResponse(
                call: Call<ArrayList<Institution>>,
                response: Response<ArrayList<Institution>>
            ) {
                institutions=response.body() as java.util.ArrayList<Institution>
                institutionsAdapter.addAll(institutions)
            }

            override fun onFailure(call: Call<ArrayList<Institution>>, t: Throwable) {
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