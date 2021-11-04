package com.example.kotlin_basic_chat.Chat.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kotlin_basic_chat.contain.Define.Companion.EMPTY_STR

@Entity(tableName = "tb_chat")
data class ChatData(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 0,

    @ColumnInfo(name = "nickname")
    val nickname: String? = EMPTY_STR,

    @ColumnInfo(name = "message")
    val message: String? = EMPTY_STR
)
