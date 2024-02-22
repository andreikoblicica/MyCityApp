package com.example.communityappmobile.services.ui.events

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.communityappmobile.R
import com.example.communityappmobile.apis.ServerAPI
import com.example.communityappmobile.databinding.FragmentEventDetailsBinding
import com.example.communityappmobile.models.auth.User
import com.example.communityappmobile.models.events.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EventDetailsFragment : Fragment() {

    private var _binding: FragmentEventDetailsBinding? = null

    private val binding get() = _binding!!

    private lateinit var event: Event

    private var userEvent:Boolean = false

    private var favorite: Boolean=false

    val serverAPI= ServerAPI.createApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        event = (arguments?.getSerializable("event") as? Event)!!
        userEvent=(arguments?.getSerializable("user_event") as? Boolean)!!

        _binding = FragmentEventDetailsBinding.inflate(inflater, container, false)

        binding.eventDetailsType.text=event.type.uppercase()
        binding.eventDetailsTitle.text=event.title

        var endDate=""
        if(event.endDateTime!=""){
            endDate=" - "+event.endDateTime
        }
        binding.eventDetailsDate.text=event.startDateTime+endDate
        binding.eventDetailsLocation.text=event.location
        Glide.with(this).load(event.image).into(binding.eventDetailsImage)
        binding.eventDetailsDescription.text=event.description

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate ( R.menu.event_details_menu, menu )
        if(userEvent){
            menu.getItem(0).setIcon(R.drawable.ic_delete)
        }else{
            serverAPI.isFavorite(User.id,event.id).enqueue(object:
                Callback<Boolean> {
                override fun onResponse(
                    call: Call<Boolean>,
                    response: Response<Boolean>
                ) {
                    if(response.isSuccessful){
                        favorite=response.body() as Boolean
                        if(favorite){
                            menu.getItem(0).setIcon(R.drawable.ic_favorite)
                        }
                        else{
                            menu.getItem(0).setIcon(R.drawable.ic_favorite_outline)
                        }
                    }else{
                        Log.e("GET_FAIL",response.toString())
                        menu.getItem(0).setIcon(R.drawable.ic_favorite_outline)
                    }

                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.e("GET_FAIL",t.toString())
                    menu.getItem(0).setIcon(R.drawable.ic_favorite_outline)
                }

            })
        }


        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.toggle_favorite){
            if(userEvent){
                openDeleteDialog()
            }else{
                favorite=!favorite
                if(favorite){
                    addToFavorites(item)
                }else{
                    removeFromFavorites(item)
                }
            }

        }

        return super.onOptionsItemSelected(item)
    }

    private fun openDeleteDialog(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete event?")
        builder.setMessage("If you confirm, event will be permanently deleted")
        builder.setPositiveButton("Yes") { _, _ ->
            serverAPI.deleteEvent(event.id).enqueue(object:
                Callback<Void> {
                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    if(response.isSuccessful){
                        findNavController().popBackStack(R.id.navigation_my_events,false)
                        findNavController().navigate(R.id.navigation_my_events)
                        Toast.makeText(requireContext(),"Event deleted successfully", Toast.LENGTH_SHORT).show()
                    }else{
                        Log.e("DELETE_FAIL",response.toString())
                    }

                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("DELETE_FAIL",t.toString())
                }

            })

        }

        builder.setNegativeButton("No") { _, _ ->
        }
        builder.show()
    }

    private fun addToFavorites(item: MenuItem){
        serverAPI.addToFavorites(User.id,event.id).enqueue(object:
            Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if(response.isSuccessful){
                    item.setIcon(R.drawable.ic_favorite)
                    Toast.makeText(requireContext(),"Added to favorites", Toast.LENGTH_SHORT).show()
                }else{
                    Log.e("GET_FAIL",response.toString())
                }

            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("GET_FAIL",t.toString())
            }

        })
    }

    private fun removeFromFavorites(item: MenuItem){
        serverAPI.removeFromFavorites(User.id,event.id).enqueue(object:
            Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if(response.isSuccessful){
                    item.setIcon(R.drawable.ic_favorite_outline)
                    Toast.makeText(requireContext(),"Removed from favorites", Toast.LENGTH_SHORT).show()
                }else{
                    Log.e("GET_FAIL",response.toString())
                }

            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("GET_FAIL",t.toString())
            }

        })
    }






}