package com.example.kotlin_basic_chat.Chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.kotlin_basic_chat.Chat.adapter.ChatAdapter
import com.example.kotlin_basic_chat.Chat.model.ChatData
import com.example.kotlin_basic_chat.Chat.model.ChatDataBase
import com.example.kotlin_basic_chat.Chat.viewModel.ChatViewModel
import com.example.kotlin_basic_chat.R
import com.example.kotlin_basic_chat.contain.Define.Companion.MYCHAT
import com.example.kotlin_basic_chat.contain.Define.Companion.OTHERCHAT
import kotlinx.android.synthetic.main.fragment_chat.*


class ChatFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance(): ChatFragment {
            return ChatFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ChatDataBase.getInstance(requireContext())?.let {
            it.onChatRead()
        }
        ChatDataBase.getInstance(requireContext())?.chatReadData?.observe(viewLifecycleOwner, { list ->
            with(chat_recycler) {
                layoutManager = LinearLayoutManager(context)
                for(k in list) {
                    Log.d("TEST", "Nickname : ${k.nickname}")
                    when(k.nickname) {
                        MYCHAT -> adapter = ChatAdapter(requireContext(), list, MYCHAT)
                        OTHERCHAT -> adapter = ChatAdapter(requireContext(), list, OTHERCHAT)
                    }
                }
            }
        })

        chat_submit_btn.setOnClickListener {
            if(!chat_send_message_edit.text.isNullOrBlank()) {
                ChatDataBase.getInstance(requireContext())?.onChatCreate(ChatData(
                    id = null,
                    nickname = ChatDataBase.getInstance(requireContext())?.chat_user_select_tag?.value,
                    message = chat_send_message_edit.text.toString()
                ))
            }
        }
    }
}