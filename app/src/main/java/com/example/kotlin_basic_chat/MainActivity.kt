package com.example.kotlin_basic_chat

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlin_basic_chat.Chat.ChatFragment
import com.example.kotlin_basic_chat.Chat.model.ChatDataBase
import com.example.kotlin_basic_chat.Chat.viewModel.ChatViewModel
import com.example.kotlin_basic_chat.Chat.viewModel.FragmentChangeViewModel
import com.example.kotlin_basic_chat.contain.Define.Companion.CHAT
import com.example.kotlin_basic_chat.contain.Define.Companion.MYCHAT
import com.example.kotlin_basic_chat.contain.Define.Companion.OTHERCHAT
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FragmentChangeViewModel.getInstance().fragment_screen_tag.value = CHAT
        FragmentChangeViewModel.getInstance().fragment_screen_tag.observe( this, { ob ->
            changeFragment(ob)
        })

//        ChatDataBase.getInstance(this)?.chatDao()?.chatReset()

        chat_my_select.setOnClickListener { changeMyOtherSelect(MYCHAT) }
        chat_other_select.setOnClickListener { changeMyOtherSelect(OTHERCHAT) }
    }

    fun changeMyOtherSelect(target: String?) {
        when(target) {
            MYCHAT -> {
                chat_my_select.setBackgroundColor(Color.parseColor("#fef01b"))
                chat_other_select.setBackgroundColor(Color.parseColor("#eeeeee"))
                ChatDataBase.getInstance(this)?.apply {
                    chat_user_select_tag?.value = MYCHAT
                    onChatRead()
                }
            }
            OTHERCHAT -> {
                chat_other_select.setBackgroundColor(Color.parseColor("#fef01b"))
                chat_my_select.setBackgroundColor(Color.parseColor("#eeeeee"))
                ChatDataBase.getInstance(this)?.apply {
                    chat_user_select_tag?.value = OTHERCHAT
                    onChatRead()
                }
            }
        }
    }

    fun changeFragment(fragment_type: String?) {
        fragment_type?.let { ty ->
            checkFragment(ty)
        }
    }

    fun checkFragment(fragment_type:String?) {
        supportFragmentManager?.beginTransaction()?.let { ft ->
            fragment_type?.let { ty ->
                when(ty) {
                    CHAT -> {
                        ChatFragment()?.apply { ft.replace(R.id.main_frag, this).commit() }
                    }
                    else -> {}
                }
            }
        }
    }
}