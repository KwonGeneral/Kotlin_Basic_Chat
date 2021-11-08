package com.example.kotlin_basic_chat

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.kotlin_basic_chat.Chat.viewModel.FragmentChangeViewModel
import com.example.kotlin_basic_chat.contain.Define.Companion.BACKGROUND
import com.example.kotlin_basic_chat.contain.Define.Companion.CHANNEL
import com.example.kotlin_basic_chat.contain.Define.Companion.OCHANNEL
import com.example.kotlin_basic_chat.contain.Define.Companion.PUSH_CALL
import com.example.kotlin_basic_chat.contain.Define.Companion.PUSH_LINK
import com.example.kotlin_basic_chat.contain.Define.Companion.PUSH_WEB
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.*


class FcmService: FirebaseMessagingService() {
    @SuppressLint("WrongThread")
    override fun onMessageReceived(remoteMessage: RemoteMessage)
    {
        Log.d("TEST", "onMessageReceived onMessageReceived onMessageReceived")
        super.onMessageReceived(remoteMessage)
        remoteMessage.notification?.let {
            var status:String = "none"
            var click_data:String = "none"
            if(remoteMessage.data["link"] != null) {
                Log.d("TEST", "Link 들어옴 : ${remoteMessage.data["link"]}")
                status = PUSH_LINK
                click_data = remoteMessage.data["link"].toString()
            } else if (remoteMessage.data["web"] != null) {
                Log.d("TEST", "Web 들어옴 : ${remoteMessage.data["web"]}")
                status = PUSH_WEB
                click_data = remoteMessage.data["web"].toString()
            } else if (remoteMessage.data["call"] != null) {
                Log.d("TEST", "Call 들어옴 : ${remoteMessage.data["call"]}")
                status = PUSH_CALL
                click_data = remoteMessage.data["call"].toString()
            }
            sendNotification(it.title, it.body!!, status, click_data)
        }
    }

    override fun onNewToken(token: String)
    {
        super.onNewToken(token)
    }

    @SuppressLint("ObsoleteSdkInt", "UnspecifiedImmutableFlag")
    private fun sendNotification(title: String?, body: String, status:String, click_data:String)
    {
        Log.d("TEST", "sendNotification sendNotification sendNotification")
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        /*
        val pendingIntent = PendingIntent.getActivity(this, 0,
            Intent(this, MainActivity::class.java)?.apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }, PendingIntent.FLAG_ONE_SHOT)*/
        val pendingIntent = PendingIntent.getActivity(this, 0,
            Intent(Intent.ACTION_VIEW)?.apply {
                data = Uri.parse(click_data)
            }, PendingIntent.FLAG_ONE_SHOT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL,
                OCHANNEL,
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, NotificationCompat.Builder(this, CHANNEL).apply {
            setSmallIcon(R.mipmap.ic_launcher_round)
            setContentTitle(title)
            setContentText(body)
            setAutoCancel(true)
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            setContentIntent(pendingIntent)
        }.build())
    }
}
