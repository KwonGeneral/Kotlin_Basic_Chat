package com.example.kotlin_basic_chat

import android.graphics.Color
import android.os.Bundle
import com.example.kotlin_basic_chat.Chat.ChatFragment
import com.example.kotlin_basic_chat.Chat.model.ChatData
import com.example.kotlin_basic_chat.Chat.model.ChatDataBase
import com.example.kotlin_basic_chat.Chat.viewModel.FragmentChangeViewModel
import com.example.kotlin_basic_chat.contain.Define.Companion.AMRECHAT
import com.example.kotlin_basic_chat.contain.Define.Companion.CHAT
import com.example.kotlin_basic_chat.contain.Define.Companion.KANOCHAT
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FragmentChangeViewModel.getInstance().fragment_screen_tag.observe( this, { ob ->
            changeFragment(ob)
        })

        changeMyOtherSelect(AMRECHAT)
        chat_my_select.setOnClickListener { changeMyOtherSelect(AMRECHAT) }
        chat_other_select.setOnClickListener { changeMyOtherSelect(KANOCHAT) }
    }

    fun moveChatFragment() {
        supportFragmentManager?.beginTransaction()?.let { ft ->
            CoroutineScope(IO).launch {
                val chat = ChatDataBase.getInstance(this@MainActivity)?.coroutineChatRead()
                chat?.let { ChatFragment.newInstance(it)?.apply { ft.replace(R.id.main_frag, this).commit() } }
            }
        }
    }

    fun changeMyOtherSelect(target: String) {
        ChatDataBase.getInstance(this)?.let {
            when(target) {
                AMRECHAT -> {
                    chat_my_select.setBackgroundColor(Color.parseColor("#fef01b"))
                    chat_other_select.setBackgroundColor(Color.parseColor("#eeeeee"))
                    it.chat_user_select_tag?.value = AMRECHAT
//                    it.onChatRead()
                    moveChatFragment()
                }
                KANOCHAT -> {
                    chat_other_select.setBackgroundColor(Color.parseColor("#fef01b"))
                    chat_my_select.setBackgroundColor(Color.parseColor("#eeeeee"))
                    it.chat_user_select_tag?.value = KANOCHAT
//                    it.onChatRead()
                    moveChatFragment()
                }
                else -> {}
            }
        }
    }

    fun changeFragment(fragment_type: String?) {
        fragment_type?.let { ty ->
            checkFragment(ty)
        }
    }

    fun checkFragment(fragment_type:String) {
        supportFragmentManager?.beginTransaction()?.let { ft ->
            fragment_type?.let { ty ->
                when (ty) {
                    CHAT -> ChatFragment()?.apply { ft.replace(R.id.main_frag, this).commit() }
                    else -> ChatFragment()?.apply { ft.replace(R.id.main_frag, this).commit() }
                }
            }
        }
    }
}