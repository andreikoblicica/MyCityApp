package com.example.communityappmobile.services.ui.facilities


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.communityappmobile.databinding.FragmentFacilityDetailsBinding
import com.example.communityappmobile.models.elements.Facility


class FacilityDetailsFragment : Fragment(), OnClickListener {

    private var _binding: FragmentFacilityDetailsBinding? = null

    private val binding get() = _binding!!

    private lateinit var facility:Facility

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        facility = (arguments?.getSerializable("facility") as? Facility)!!
        _binding = FragmentFacilityDetailsBinding.inflate(inflater, container, false)

        val imageView=binding.facilityDetailsImage
        Glide.with(this).load(facility.imageUrl).into(imageView)

        binding.facilityDetailsName.text= facility.name
        binding.facilityDetailsType.text=facility.type.uppercase()
        binding.facilityDetailsDescription.text=facility.description
        binding.facilityDetailsLocation.text=facility.location

        binding.viewFacility.setOnClickListener(this)
        binding.getDirections.setOnClickListener(this)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(button: View?) {
        if(button is Button && button.text=="Website"){
            val uri= Uri.parse(facility.websiteLink)
            val openLinkIntent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(openLinkIntent)
        }
        else if(button is Button && button.text=="Directions"){

            val gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(facility.location))
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")

            startActivity(mapIntent)

        }






    }


}