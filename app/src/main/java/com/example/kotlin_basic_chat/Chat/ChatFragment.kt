package com.example.kotlin_basic_chat.Chat

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin_basic_chat.Chat.adapter.ChatAdapter
import com.example.kotlin_basic_chat.Chat.model.ChatData
import com.example.kotlin_basic_chat.Chat.model.ChatDataBase
import com.example.kotlin_basic_chat.R
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

        ChatDataBase.getInstance(requireContext())?.chatReadData?.observe(viewLifecycleOwner, { list ->
            with(chat_recycler) {
                layoutManager = LinearLayoutManager(context)
                adapter = ChatAdapter(requireContext(), list)
                scrollToPosition(list.size - 1)
            }
        })
        chat_submit_btn.setOnClickListener {
            chat_send_message_edit?.let {
                when(it.text.isNullOrBlank()) {
                    false -> {
                        ChatDataBase.getInstance(requireContext())?.onChatCreate(ChatData(
                            id = null,
                            nickname = ChatDataBase.getInstance(requireContext())?.chat_user_select_tag?.value,
                            message = it.text.toString()
                        ))

                        val mInputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        mInputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                        it.setText("")
                        it.clearFocus()
                    }
                    true -> return@setOnClickListener
                }
            }

        }
    }
}