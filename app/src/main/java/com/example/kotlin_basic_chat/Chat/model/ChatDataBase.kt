package com.example.kotlin_basic_chat.Chat.model

import android.content.Context
import android.service.autofill.UserData
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kotlin_basic_chat.contain.Define.Companion.MYCHAT


@Database(entities = [ChatData::class], version = 1, exportSchema = false)
abstract class ChatDataBase: RoomDatabase() {
    abstract fun chatDao(): ChatDao
    var chatReadData = MutableLiveData<List<ChatData>>()
    var chat_user_select_tag = MutableLiveData<String>(MYCHAT)

    companion object {
        var instance: ChatDataBase? = null
        @Synchronized
        fun getInstance(context: Context): ChatDataBase? {
            instance?.let{
                return it
            }
            instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChatDataBase::class.java,
                    "chat_db"
                )
                .fallbackToDestructiveMigration()  // 스키마(=테이블 설계 구조) 버전 변경 가능
                .allowMainThreadQueries()  // Main Thread에서 DB에 IO(Input/Output)를 가능하게 함
                .build()
            return instance
        }
    }

    fun onChatCreate(chat: ChatData) {
        Log.d("TEST", "생성 : $chat")
        chatDao()?.apply {
            chatCreate(chat)
            chatReadData.value = chatReadAll()
        }
    }

    fun onChatRead() {
        chatDao()?.apply {
            chatReadData.value = chatReadAll()
            Log.d("TEST", "읽기 : ${chatReadData.value}")
        }
    }

    fun onChatDelete() {

    }

    fun onChatSearch() {

    }
}