package com.example.kotlin_basic_chat.Chat.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_basic_chat.Chat.model.ChatData
import com.example.kotlin_basic_chat.R
import com.example.kotlin_basic_chat.contain.Define.Companion.MYCHAT
import com.example.kotlin_basic_chat.contain.Define.Companion.OTHERCHAT
import kotlinx.android.synthetic.main.fragment_chat_my_item.view.*

class ChatAdapter constructor(var context: Context, var items:List<ChatData>, var user_select_tag: String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d("TEST", "user_select_tag -> $user_select_tag")
        when(user_select_tag) {
            MYCHAT -> return VH(LayoutInflater.from(context).inflate(R.layout.fragment_chat_my_item, parent, false))
            OTHERCHAT -> return VH(LayoutInflater.from(context).inflate(R.layout.fragment_chat_other_item, parent, false))
        }
        return VH(LayoutInflater.from(context).inflate(R.layout.fragment_chat_my_item, parent, false))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh: VH = holder as VH

        items[position]?.let { item ->
            Log.d("TEST", "???? : $item")
            with(vh.itemView) {
                chat_nickname.text = item.nickname
                chat_text.text = item.message
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class VH constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
    }
}