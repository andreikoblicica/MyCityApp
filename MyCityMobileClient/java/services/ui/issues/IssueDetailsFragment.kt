package com.example.communityappmobile.services.ui.issues


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.communityappmobile.R
import com.example.communityappmobile.adapters.InvolvedInstitutionsAdapter
import com.example.communityappmobile.databinding.FragmentIssueDetailsBinding
import com.example.communityappmobile.models.issue.InvolvedInstitution
import com.example.communityappmobile.models.issue.Issue
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class IssueDetailsFragment: Fragment(),OnMapReadyCallback,OnMapClickListener {

    private var _binding: FragmentIssueDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var issue: Issue

    private lateinit var latLng: LatLng

    private lateinit var googleMap: GoogleMap
    lateinit var supportMapFragment: SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        issue = ((arguments?.getSerializable("issue") as? Issue)!!)
        _binding = FragmentIssueDetailsBinding.inflate(inflater, container, false)

        binding.issueDetailsStatus.text=issue.status
        binding.issueDetailsTitle.text=issue.title
        binding.issueDetailsType.text=issue.type.uppercase()
        binding.issueDetailsDescription.text=issue.description
        binding.issueDetailsDatetime.text=issue.dateTime
        binding.issueDetailsLocation.text=issue.location

        supportMapFragment= childFragmentManager.findFragmentById(R.id.issue_details_location_map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)

        val str=issue.coordinates.split(',')
        latLng=LatLng(str[0].toDouble(),str[1].toDouble())


        val involvedInstitutionsRecyclerView: RecyclerView = binding.involvedInstitutionsList
        val emptyList: java.util.ArrayList<InvolvedInstitution> = arrayListOf()
        val involvedInstitutionsAdapter = InvolvedInstitutionsAdapter(requireContext(),emptyList)
        involvedInstitutionsRecyclerView.adapter = involvedInstitutionsAdapter
        involvedInstitutionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        involvedInstitutionsAdapter.addAll(issue.involvedInstitutions)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate ( R.menu.issue_details_menu, menu )
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.go_to_issue_chat){
            val bundle = Bundle().apply {
                putSerializable("issue", issue)
            }
            val issueChatFragment = IssueChatFragment()
            issueChatFragment.arguments = bundle
            findNavController().navigate(R.id.navigation_issue_chat, bundle)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.setOnMapClickListener(this)

        val markerOptions= MarkerOptions()
        markerOptions.position(latLng)
        googleMap.addMarker(markerOptions)
        val cameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(15f)
            .build()
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onMapClick(p0: LatLng) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(issue.coordinates))
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        startActivity(mapIntent)
    }


}