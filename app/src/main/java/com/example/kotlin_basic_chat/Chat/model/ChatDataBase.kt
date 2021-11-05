package com.example.kotlin_basic_chat.Chat.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kotlin_basic_chat.contain.Define.Companion.AMRECHAT
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.yield


@Database(entities = [ChatData::class], version = 1, exportSchema = false)
abstract class ChatDataBase: RoomDatabase() {
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

    fun onChatCreate(chat: ChatData) {
        chatDao()?.apply {
            chatCreate(chat)
            chatReadData.value = chatReadAll()
        }
    }

    fun onChatRead() {
        chatDao()?.apply {
            chatReadData.value = chatReadAll()
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