package com.example.kotlin_basic_chat.Chat.viewModel

import androidx.lifecycle.MutableLiveData
import com.example.kotlin_basic_chat.Chat.model.ChatData

class SearchViewModel {
    var search_result = MutableLiveData<List<ChatData>>()

    companion object {
        var searchViewModel: SearchViewModel? = null
        fun getInstance(): SearchViewModel {
            searchViewModel?.let {
                return it
            }
            searchViewModel = SearchViewModel()
            return searchViewModel!!
        }
    }
}