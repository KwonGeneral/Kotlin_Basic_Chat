package com.example.kotlin_basic_chat.Chat.viewModel

import androidx.lifecycle.MutableLiveData
import com.example.kotlin_basic_chat.Chat.model.ChatData

class ChatViewModel {

    var search_result = MutableLiveData<List<ChatData>>()

    companion object {
        var chatViewModel: ChatViewModel? = null
        fun getInstance(): ChatViewModel {
            chatViewModel?.let {
                return it
            }
            chatViewModel = ChatViewModel()
            return chatViewModel!!
        }
    }
}