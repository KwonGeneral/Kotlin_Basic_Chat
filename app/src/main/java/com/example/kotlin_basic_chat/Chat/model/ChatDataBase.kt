package com.example.kotlin_basic_chat.Chat.model

import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kotlin_basic_chat.contain.Define.Companion.AMRECHAT
import kotlinx.coroutines.*


@Database(entities = [ChatData::class], version = 1, exportSchema = false)
abstract class ChatDataBase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    var chatReadData = MutableLiveData<List<ChatData>>()
    var chat_user_select_tag = MutableLiveData(AMRECHAT)

    companion object {
        var instance: ChatDataBase? = null

        @Synchronized
        fun getInstance(context: Context): ChatDataBase? {
            instance?.let {
                return it
            }
            instance = Room.databaseBuilder(
                context.applicationContext,
                ChatDataBase::class.java,
                "chat_db"
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
            return instance
        }
    }

    fun onChatCreate(message: String) {

        onChatCreate(
            ChatData(
                id = null,
                nickname = chat_user_select_tag?.value,
                message = message
            )
        )
        CoroutineScope(Dispatchers.IO).launch {
            onChatRead()
        }
    }

    fun onChatCreate(chat: ChatData) {
        chatDao()?.apply {
            chatCreate(chat)
            chatReadData.postValue(chatReadAll())
        }
    }

     suspend fun onChatRead() {

        chatDao()?.apply {
            chatReadData.postValue(coroutineChatRead())
        }
    }


    suspend fun coroutineChatRead(): List<ChatData> {
        return coroutineScope {
            val k = async { chatDao()?.chatReadAll() }
            yield()
            return@coroutineScope k.await()
        }
    }
}