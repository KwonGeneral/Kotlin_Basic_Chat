package com.example.kotlin_basic_chat.Chat.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_basic_chat.Chat.model.ChatData
import com.example.kotlin_basic_chat.Chat.model.ChatDataBase
import com.example.kotlin_basic_chat.R
import com.example.kotlin_basic_chat.contain.Define.Companion.AMRECHAT
import com.example.kotlin_basic_chat.contain.Define.Companion.KANOCHAT
import kotlinx.android.synthetic.main.fragment_chat_my_item.view.*

class ChatAdapter constructor(var context: Context, var items:List<ChatData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun notifyChanges(oldList: List<ChatData>, newList: List<ChatData>) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].id == newList[newItemPosition].id
            }
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }
            override fun getOldListSize() = oldList.size
            override fun getNewListSize() = newList.size
        })
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            0 -> VH(LayoutInflater.from(context).inflate(R.layout.fragment_chat_my_item, parent, false))
            1 -> VH(LayoutInflater.from(context).inflate(R.layout.fragment_chat_other_item, parent, false))
            else -> VH(LayoutInflater.from(context).inflate(R.layout.fragment_chat_my_item, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        when(ChatDataBase.getInstance(context)?.chat_user_select_tag?.value) {
            AMRECHAT -> {
                if(items[position].nickname == AMRECHAT) return 0
                else if(items[position].nickname == KANOCHAT) return 1
            }
            KANOCHAT -> {
                if(items[position].nickname == AMRECHAT) return 1
                else if(items[position].nickname == KANOCHAT) return 0
            }
        }
        return 0
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            items[position]?.let { item ->
                with(holder.itemView) {
                    chat_nickname.text = item.nickname
                    chat_text.text = item.message
                }
            }
    }
    override fun getItemCount(): Int {
        return items.size
    }
    class VH constructor(itemView: View) : RecyclerView.ViewHolder(itemView)
}