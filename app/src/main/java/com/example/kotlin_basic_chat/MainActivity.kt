package com.example.kotlin_basic_chat

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kotlin_basic_chat.Chat.ChatFragment
import com.example.kotlin_basic_chat.Chat.model.ChatDataBase
import com.example.kotlin_basic_chat.Chat.viewModel.FragmentChangeViewModel
import com.example.kotlin_basic_chat.contain.Define.Companion.AMRECHAT
import com.example.kotlin_basic_chat.contain.Define.Companion.BACKGROUND
import com.example.kotlin_basic_chat.contain.Define.Companion.CHAT
import com.example.kotlin_basic_chat.contain.Define.Companion.FOREGROUND
import com.example.kotlin_basic_chat.contain.Define.Companion.KANOCHAT
import com.example.kotlin_basic_chat.contain.Define.Companion.WEB
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap



class MainActivity : BaseActivity() {
    var firebaseDB = FirebaseFirestore.getInstance()

    override fun onResume() {
        Log.d("TEST", "onResume, onResume, onResume")

        super.onResume()
    }

    override fun onPause() {
        Log.d("TEST", "onPause, onPause, onPause")
        super.onPause()
    }

    override fun onDestroy() {
        Log.d("TEST", "onDestroy, onDestroy, onDestroy")
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FragmentChangeViewModel.getInstance()?.let { fc ->
            fc.fragment_screen_tag.observe( this, { ob ->
                changeFragment(ob)
            })
        }

        intent.getStringExtra("link")?.let {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
        }

        changeMyOtherSelect(AMRECHAT)
        chat_my_select.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.naver.com")))
//            changeMyOtherSelect(AMRECHAT)
        }
        chat_other_select.setOnClickListener { changeMyOtherSelect(KANOCHAT) }
//        ChatDataBase.getInstance(this)?.chatDao()?.chatReset()

        // Fcm 토큰 생성 및 Firebase DB 저장
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            var fcm_list = mutableListOf<String>()
            firebaseDB.collection("User")?.let { db ->
                db.get().addOnCompleteListener { read ->
                    if (read.isSuccessful) {
                        for (document in read.result!!) {
                            fcm_list.add(document.data["fcmToken"].toString())
                        }

                        task.result.toString()?.let { fcm ->
                            if(fcm in fcm_list) {
                                return@addOnCompleteListener
                            }
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))?.let { time ->
                                Build.MODEL?.let { model ->
                                    val user: MutableMap<String, Any> = HashMap()
                                    user["fcmToken"] = fcm
                                    user["phoneModel"] = model
                                    user["createdAt"] = time

                                    db.add(user)?.apply {
                                        addOnSuccessListener {
                                            Toast.makeText(baseContext, "FCM 토큰을 Firebase DB에 저장했습니다", Toast.LENGTH_SHORT).show()
                                        }
                                        addOnFailureListener {
                                            Toast.makeText(baseContext, "FCM 토큰을 Firebase DB 저장에 실패했습니다", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        })
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

    fun changeFragment(fragment_type: String?, link: String = "") {
        if(link != "") {
            fragment_type?.let { ty ->
                checkFragment(ty, link)
            }
        }else {
            fragment_type?.let { ty ->
                checkFragment(ty)
            }
        }
    }

    fun checkFragment(fragment_type:String, link:String = "") {
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