package com.example.communityappmobile.services.ui.issues


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.communityappmobile.R
import com.example.communityappmobile.adapters.IssuesAdapter
import com.example.communityappmobile.apis.ServerAPI
import com.example.communityappmobile.databinding.FragmentIssuesBinding
import com.example.communityappmobile.models.auth.User
import com.example.communityappmobile.models.issue.Issue
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IssuesFragment: Fragment(), OnClickListener {

    private var _binding: FragmentIssuesBinding? = null

    private val binding get() = _binding!!

    private lateinit var issuesAdapter: IssuesAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentIssuesBinding.inflate(inflater, container, false)

        val issuesRecyclerView: RecyclerView = binding.issuesRecyclerView
        val emptyList: java.util.ArrayList<Issue> = arrayListOf()

        issuesAdapter = IssuesAdapter(requireContext(),emptyList,findNavController())
        issuesRecyclerView.adapter = issuesAdapter
        issuesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val helper=binding.issuesHelperText

        var issues: ArrayList<Issue>
        val serverAPI= ServerAPI.createApi()
        serverAPI.getIssues(User.username).enqueue(object:
            Callback<ArrayList<Issue>> {
            override fun onResponse(
                call: Call<ArrayList<Issue>>,
                response: Response<ArrayList<Issue>>
            ) {
                issues=response.body() as java.util.ArrayList<Issue>
                issuesAdapter.addAll(issues.reversed() as java.util.ArrayList<Issue>)

                if(issues.size==0){
                    helper.visibility= VISIBLE
                }else{
                    helper.visibility= GONE
                }
            }

            override fun onFailure(call: Call<ArrayList<Issue>>, t: Throwable) {
                Log.e("GET_FAIL",t.toString())
            }

        })

        val reportIssueButton=binding.reportIssueButton
        reportIssueButton.setOnClickListener(this)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(p0: View?) {
        if(p0 is FloatingActionButton){
            val bundle = Bundle().apply {
                putSerializable("new_form", true)
            }

            val reportIssueFragment = ReportIssueFragment()
            reportIssueFragment.arguments = bundle
            findNavController().navigate(R.id.navigation_report_issue,bundle)
        }

    }

}
