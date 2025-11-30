package com.medicinereminder.manager

import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.Locale

/**
 * TextToSpeech管理器
 * 用于处理文本到语音的转换，包括初始化、检测TTS组件、播放语音等功能
 */
class TextToSpeechManager(private val context: Context) {

    // TTS状态
    sealed class TtsStatus {
        object Ready : TtsStatus()
        object Error : TtsStatus()
        object MissingComponent : TtsStatus()
        object Initializing : TtsStatus()
    }

    // TTS实例
    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private val statusChannel = Channel<TtsStatus>(capacity = Channel.CONFLATED)
    val statusFlow = statusChannel.receiveAsFlow()

    /**
     * 初始化TTS
     */
    fun initialize() {
        statusChannel.trySend(TtsStatus.Initializing)
        
        tts = TextToSpeech(context) { status ->
            when (status) {
                TextToSpeech.SUCCESS -> {
                    // 设置语言
                    val result = tts?.setLanguage(Locale.CHINA)
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "中文语言包缺失或不支持")
                        statusChannel.trySend(TtsStatus.MissingComponent)
                    } else {
                        isInitialized = true
                        statusChannel.trySend(TtsStatus.Ready)
                    }
                }
                TextToSpeech.ERROR -> {
                    Log.e("TTS", "TTS初始化失败")
                    statusChannel.trySend(TtsStatus.Error)
                }
                else -> {
                    Log.e("TTS", "TTS初始化状态未知: $status")
                    statusChannel.trySend(TtsStatus.Error)
                }
            }
        }

        // 设置进度监听器
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                Log.d("TTS", "开始播放: $utteranceId")
            }

            override fun onDone(utteranceId: String?) {
                Log.d("TTS", "播放完成: $utteranceId")
            }

            override fun onError(utteranceId: String?) {
                Log.e("TTS", "播放错误: $utteranceId")
            }
        })
    }

    /**
     * 检查系统是否有TTS组件
     */
    fun isTtsAvailable(): Boolean {
        val ttsIntent = Intent()
        ttsIntent.action = TextToSpeech.Engine.ACTION_CHECK_TTS_DATA
        return context.packageManager.resolveActivity(ttsIntent, 0) != null
    }

    /**
     * 播放语音
     * @param text 要播放的文本
     */
    fun speak(text: String) {
        if (isInitialized && tts != null) {
            tts?.speak(text, TextToSpeech.QUEUE_ADD, null, "utterance_${System.currentTimeMillis()}")
        }
    }

    /**
     * 停止播放
     */
    fun stop() {
        tts?.stop()
    }

    /**
     * 关闭TTS
     */
    fun shutdown() {
        tts?.shutdown()
        isInitialized = false
    }
}
