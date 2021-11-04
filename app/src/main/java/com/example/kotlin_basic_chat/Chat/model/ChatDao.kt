package com.example.kotlin_basic_chat.Chat.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface ChatDao {
    @Query("SELECT * FROM tb_chat")
    fun chatReadAll(): List<ChatData>

    @Insert
    fun chatCreate(vararg user: ChatData)

    @Query("SELECT * FROM tb_chat WHERE message = :message")
    fun chatSearch(message: String): List<ChatData>

    @Query("DELETE FROM tb_chat")
    fun chatReset()
}