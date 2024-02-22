package com.example.communityappmobile.services.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.example.communityappmobile.R
import com.example.communityappmobile.adapters.MainMenuAdapter
import com.example.communityappmobile.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val mainMenuListView: ListView = binding.mainMenuOptionsList
        val mainMenuAdapter = MainMenuAdapter(requireContext(), R.layout.item_main_menu,homeViewModel.getMenuOptions())
        mainMenuListView.adapter = mainMenuAdapter

        mainMenuListView.setOnItemClickListener { adapterView, view, i, l ->
            val selectedMenuOption = homeViewModel.getMenuOptions()[i]

            when (selectedMenuOption.getTitle()) {
                "Issues" -> findNavController().navigate(R.id.navigation_issues)
                "Facilities" -> findNavController().navigate(R.id.navigation_facilities)
                "Institutions" -> findNavController().navigate(R.id.navigation_institutions)
                "Events" -> findNavController().navigate(R.id.navigation_events)
                "News" -> findNavController().navigate(R.id.navigation_news)
            } }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}