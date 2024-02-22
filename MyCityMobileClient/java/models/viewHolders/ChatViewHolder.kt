package com.example.communityappmobile.models.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.communityappmobile.R
import com.example.communityappmobile.adapters.ChatAdapter
import com.example.communityappmobile.models.issue.Message

class ChatViewHolder(private val view: View, private val adapter: ChatAdapter)  :
    RecyclerView.ViewHolder(view) {
    private var sender: TextView
    private var text: TextView
    private var timestamp: TextView


    init {
        sender = view.findViewById(R.id.message_sender)
        text=view.findViewById(R.id.message_text)
        timestamp=view.findViewById(R.id.message_timestamp)
    }

    fun bindData(message: Message) {
        sender.text = message.sourceName
        text.text=message.message
        timestamp.text=message.timestamp
    }






}