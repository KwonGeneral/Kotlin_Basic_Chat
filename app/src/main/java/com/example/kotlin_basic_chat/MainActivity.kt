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

        chat_my_select.setOnClickListener { changeMyOtherSelect(AMRECHAT) }
        chat_other_select.setOnClickListener { changeMyOtherSelect(KANOCHAT) }

        //화면 갱신 시 호출
        ChatDataBase.getInstance(this)?.chat_user_select_tag?.observe(this,{
            chat_other_select.setBackgroundColor(Color.parseColor("#eeeeee"))
            var colorString:String = "#fef01b"
            when(it) {
                AMRECHAT -> {
                    colorString =   "#fef01b"
                }
                KANOCHAT -> {
                    colorString =   "#fef01b"
                }
                else -> {}
            }
            chat_my_select.setBackgroundColor(Color.parseColor(colorString))
            supportFragmentManager?.beginTransaction()?.let { ft ->
                ChatFragment.newInstance()?.apply {
                    ft.replace(R.id.main_frag, this).commit()
                }
            }
        })
        changeMyOtherSelect(AMRECHAT)
    }

    fun changeMyOtherSelect(target: String) {
        ChatDataBase.getInstance(this)?.chat_user_select_tag?.value = target
    }
}