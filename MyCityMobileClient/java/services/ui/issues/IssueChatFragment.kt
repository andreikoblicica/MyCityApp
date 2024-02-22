package com.example.communityappmobile.services.ui.issues

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.OnClickListener
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.communityappmobile.adapters.ChatAdapter
import com.example.communityappmobile.apis.ServerAPI
import com.example.communityappmobile.databinding.FragmentIssueChatBinding

import com.example.communityappmobile.models.auth.User
import com.example.communityappmobile.models.issue.Issue
import com.example.communityappmobile.models.issue.Message
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class IssueChatFragment: Fragment(), OnClickListener {

    private var _binding: FragmentIssueChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var messagesRecyclerView: RecyclerView

    private lateinit var sendButton: FloatingActionButton
    private lateinit var messageTextView: EditText

    private lateinit var issue:Issue

    private lateinit var helper: TextView

    val dateTimeFormatter = SimpleDateFormat("d MMM, HH:mm", Locale.US)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        issue = (arguments?.getSerializable("issue") as?  Issue)!!
        _binding = FragmentIssueChatBinding.inflate(inflater, container, false)

        messagesRecyclerView = binding.messagesRecyclerView

        chatAdapter = ChatAdapter(requireContext(),issue.messages)

        messagesRecyclerView.adapter = chatAdapter
        messagesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        if(chatAdapter.itemCount>0){
            messagesRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
        }

        sendButton=binding.sendMessageButton
        sendButton.setOnClickListener(this)

        messageTextView=binding.chatMessageText

        helper=binding.chatHelperText
        if(issue.messages.size==0){
            helper.visibility= View.VISIBLE
        }else{
            helper.visibility= View.GONE
        }

        return binding.root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(p0: View?) {
        if(p0 is FloatingActionButton){
            val date= Date()
            val message=Message(
                issue.id,
                User.username,
                issue.involvedInstitutions.map { item->item.institutionName } as ArrayList<String>,
                messageTextView.text.toString(),
                dateTimeFormatter.format(date))

            sendMessage(message)

        }
    }

    private fun sendMessage(message:Message){
        val serverAPI= ServerAPI.createApi()
        serverAPI.sendMessage(message).enqueue(object:
            Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if(response.isSuccessful){
                    helper.visibility= View.GONE
                    chatAdapter.addItem(message,true)
                    messagesRecyclerView.smoothScrollToPosition(chatAdapter.itemCount-1)
                    messageTextView.setText("")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ERR",t.toString())
            }

        })
    }





}