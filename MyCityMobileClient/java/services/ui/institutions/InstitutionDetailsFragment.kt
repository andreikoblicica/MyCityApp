package com.example.communityappmobile.services.ui.institutions

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.communityappmobile.databinding.FragmentInstitutionDetailsBinding
import com.example.communityappmobile.models.institutions.Institution

class InstitutionDetailsFragment : Fragment() {

    private var _binding: FragmentInstitutionDetailsBinding? = null

    private val binding get() = _binding!!

    private lateinit var institution: Institution

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        institution = (arguments?.getSerializable("institution") as? Institution)!!
        _binding = FragmentInstitutionDetailsBinding.inflate(inflater, container, false)


        val imageView=binding.institutionDetailsImage
        Glide.with(this).load(institution.image).into(imageView)

        binding.institutionDetailsName.text = institution.name
        binding.institutionDetailsAddress.text = institution.address
        binding.institutionDetailsEmail.text = institution.email
        binding.institutionDetailsPhone.text = institution.phoneNumber


        val directionsButton=binding.getInstitutionDirections
        directionsButton.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(institution.name))
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")

            startActivity(mapIntent)
        }

        val websiteButton=binding.viewInstitutionWebsite
        websiteButton.setOnClickListener {
            val uri= Uri.parse(institution.website)
            val openLinkIntent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(openLinkIntent)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}