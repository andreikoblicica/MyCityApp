package com.example.communityappmobile.services.ui.issues

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.communityappmobile.R
import com.example.communityappmobile.databinding.FragmentIssueLocationBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton


class IssueLocationFragment: Fragment(), OnMapReadyCallback, OnMapClickListener, OnClickListener {

    private var _binding: FragmentIssueLocationBinding? = null

    private val binding get() = _binding!!

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null

    private var isMoved=false
    private var isClicked=false

    private lateinit var currentLocation:LatLng

    private lateinit var googleMap: GoogleMap
    lateinit var supportMapFragment: SupportMapFragment
    private lateinit var markerOptions:MarkerOptions

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        requestPermissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    googleMap.isMyLocationEnabled=true

                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
                    locationCallback = object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            val location: Location? = locationResult.lastLocation
                            if (location != null) {
                                moveToCurrentLocation(location)
                            }
                        }
                    }
                    startLocationUpdates()
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    moveToPredefinedLocation()
                } else -> {
             moveToPredefinedLocation()
            }
            }
        }
    }

    private fun moveToCurrentLocation(location: Location) {
        val latitude: Double = location.latitude
        val longitude: Double = location.longitude
        currentLocation=LatLng(latitude,longitude)

        if(!isMoved){
            val cameraPosition = CameraPosition.Builder()
                .target(currentLocation)
                .zoom(15f)
                .build()
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            isMoved=true
        }
    }

    private fun moveToPredefinedLocation() {
        googleMap.isMyLocationEnabled=true
        val location = LatLng(46.775713, 23.605745)

        val cameraPosition = CameraPosition.Builder()
            .target(location)
            .zoom(15f)
            .build()
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentIssueLocationBinding.inflate(inflater, container, false)

       supportMapFragment= childFragmentManager.findFragmentById(R.id.issue_location_map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
        val confirmButton=binding.confirmLocation
        confirmButton.setOnClickListener(this)

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.navigation_report_issue,false)
                findNavController().navigate(R.id.navigation_report_issue)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.setOnMapClickListener(this)

        requestPermissionLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))

    }

    override fun onMapClick(latLng: LatLng){
        isClicked=true
        markerOptions= MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title(latLng.latitude.toString()+" "+latLng.longitude.toString())
        googleMap.clear()
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.addMarker(markerOptions)
    }

    override fun onClick(p0: View?) {
        if(p0 is FloatingActionButton){
            if(isClicked){
                val args = Bundle()
                args.putParcelable("coordinates", markerOptions.position)
                stopLocationUpdates()
                findNavController().popBackStack(R.id.navigation_report_issue,false)
                findNavController().navigate(R.id.navigation_report_issue, args)
            }else{
                if(isMoved){
                    val args = Bundle()
                    args.putParcelable("coordinates", currentLocation)
                    stopLocationUpdates()
                    findNavController().popBackStack(R.id.navigation_report_issue,false)
                    findNavController().navigate(R.id.navigation_report_issue, args)
                }
                else{
                    Toast.makeText(requireContext(),"Put a marker on the map or enable your location",Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

    private fun startLocationUpdates() {
        val locationRequest= LocationRequest.Builder(5000)
            .build()
        fusedLocationClient!!.requestLocationUpdates(
            locationRequest,
            locationCallback!!,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient!!.removeLocationUpdates(locationCallback!!)
    }




}