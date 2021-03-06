package com.example.kotlin_basic_chat.Chat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin_basic_chat.Chat.adapter.ChatAdapter
import com.example.kotlin_basic_chat.Chat.model.ChatData
import com.example.kotlin_basic_chat.Chat.model.ChatDataBase
import com.example.kotlin_basic_chat.R
import com.example.kotlin_basic_chat.TTS
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO


class ChatFragment : Fragment() {
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognitionListener: RecognitionListener

    companion object {
        @JvmStatic
        fun newInstance(list: List<ChatData>): ChatFragment {
            return ChatFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TEST", "onViewCreated onViewCreated onViewCreated")

        var intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, requireContext().packageName)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")

        setListener()

        voice_btn.setOnClickListener {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
            speechRecognizer.setRecognitionListener(recognitionListener)
            speechRecognizer.startListening(intent)
        }

        ChatDataBase.getInstance(requireContext())?.let { model->
            model.chatReadData?.observe(viewLifecycleOwner, { list ->
                with(chat_recycler) {
                    layoutManager = LinearLayoutManager(context)
                    adapter = ChatAdapter(requireContext(), list)
                    scrollToPosition(list.size - 1)
                }
            })
            CoroutineScope(IO).launch {
                model.onChatRead()
            }

            chat_submit_btn.setOnClickListener {
                chat_send_message_edit?.let {
                    when(it.text.isNullOrBlank()) {
                        false -> {
                            TTS(requireContext() as Activity, it.text.toString())
                            model.onChatCreate(it.text.toString())
                            val mInputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            mInputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                            it.setText("")
                            it.clearFocus()
                        }
                        true -> return@setOnClickListener
                    }
                }
            }
        }

    }

    private fun setListener() {
        recognitionListener = object: RecognitionListener {

            override fun onReadyForSpeech(params: Bundle?) {
                Toast.makeText(requireContext(), "??????????????? ???????????????.", Toast.LENGTH_SHORT).show()
            }

            override fun onBeginningOfSpeech() {
            }

            override fun onRmsChanged(rmsdB: Float) {
            }

            override fun onBufferReceived(buffer: ByteArray?) {
            }

            override fun onEndOfSpeech() {
            }

            override fun onError(error: Int) {
                var message: String

                when (error) {
                    SpeechRecognizer.ERROR_AUDIO ->
                        message = "????????? ??????"
                    SpeechRecognizer.ERROR_CLIENT ->
                        message = "??????????????? ??????"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS ->
                        message = "????????? ??????"
                    SpeechRecognizer.ERROR_NETWORK ->
                        message = "???????????? ??????"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT ->
                        message = "???????????? ????????????"
                    SpeechRecognizer.ERROR_NO_MATCH ->
                        message = "?????? ??? ??????"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY ->
                        message = "RECOGNIZER??? ??????"
                    SpeechRecognizer.ERROR_SERVER ->
                        message = "????????? ?????????"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT ->
                        message = "????????? ????????????"
                    else ->
                        message = "??? ??? ?????? ??????"
                }
                Toast.makeText(requireContext(), "?????? ?????? $message", Toast.LENGTH_SHORT).show()
            }

            override fun onResults(results: Bundle?) {
                var matches: ArrayList<String> = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) as ArrayList<String>

                for (i in 0 until matches.size) {
                    chat_send_message_edit.setText(matches[i])
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
            }

        }
    }
}