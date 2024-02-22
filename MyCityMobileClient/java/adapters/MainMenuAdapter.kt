package com.example.communityappmobile.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.communityappmobile.R
import com.example.communityappmobile.models.elements.MainMenuOption

class MainMenuAdapter(
    context: Context,
    resource:Int,
    objects:ArrayList<MainMenuOption>)
    : ArrayAdapter<MainMenuOption>(context,resource,objects) {
    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView=convertView ?: inflater.inflate(
            R.layout.item_main_menu,
            parent, false)
        rowView.findViewById<TextView>(R.id.main_menu_option_title).text=getItem(position)!!.getTitle()
        rowView.findViewById<ImageView>(R.id.main_menu_option_image).setImageResource(getItem(position)!!.getImageRes())
        return rowView
    }


}