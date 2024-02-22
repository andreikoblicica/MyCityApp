package com.example.communityappmobile.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.communityappmobile.R
import com.example.communityappmobile.models.auth.User
import com.example.communityappmobile.models.events.Event
import com.example.communityappmobile.models.issue.Message
import com.example.communityappmobile.models.viewHolders.ChatViewHolder
import com.example.communityappmobile.models.viewHolders.EventViewHolder
import com.example.communityappmobile.services.ui.events.EventDetailsFragment
import java.util.ArrayList

@SuppressLint("NotifyDataSetChanged")
class ChatAdapter(
    private val context: Context,
    private var dataSource: ArrayList<Message>
) : RecyclerView.Adapter<ChatViewHolder>() {


    var messages = ArrayList<Message>()
    val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    init {
        messages = dataSource
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = inflater.inflate(viewType, parent, false)
        return ChatViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bindData(messages.get(position))
    }

    override fun getItemViewType(position: Int): Int {
        if (messages[position].sourceName == User.username) {
            return R.layout.list_item_sent_message
        }
        return R.layout.list_item_received_message

    }


    override fun getItemCount(): Int {
        return messages.size
    }


    fun addItem(message: Message, atEnd: Boolean) {
        if (atEnd) {
            messages.add(message)
        } else {
            messages.add(0, message)
        }
        notifyDataSetChanged()
    }

    fun addAll(messages: ArrayList<Message>) {
        messages.addAll(messages)
        notifyDataSetChanged()
    }
}