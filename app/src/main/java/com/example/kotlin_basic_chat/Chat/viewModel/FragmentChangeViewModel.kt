package com.example.kotlin_basic_chat.Chat.viewModel

import androidx.lifecycle.MutableLiveData
import com.example.kotlin_basic_chat.contain.Define.Companion.CHAT

class FragmentChangeViewModel {
    var fragment_screen_tag = MutableLiveData(CHAT)

    companion object {
        var instance: FragmentChangeViewModel? = null

        @JvmName("fragment_getInstance")
        fun getInstance(): FragmentChangeViewModel {
            instance?.let {
                return it
            }
            instance = FragmentChangeViewModel()
            return instance!!
        }
    }
}