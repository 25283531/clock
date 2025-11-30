package com.medicinereminder.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.medicinereminder.R
import com.medicinereminder.ui.MainActivity
import java.util.Locale

/**
 * 提醒通知服务
 * 用于显示服药通知，支持自定义通知内容和点击事件
 */
class ReminderNotificationService(private val context: Context) {

    companion object {
        // 通知渠道ID
        private const val CHANNEL_ID = "medicine_reminder_channel"
        // 通知渠道名称
        private const val CHANNEL_NAME = "服药提醒"
        // 通知渠道描述
        private const val CHANNEL_DESCRIPTION = "用于提醒用户服药的通知"
        // 通知ID前缀
        private const val NOTIFICATION_ID_PREFIX = 1000
    }

    // TTS实例
    private var tts: TextToSpeech? = null

    init {
        // 创建通知渠道（Android 8.0及以上需要）
        createNotificationChannel()
        // 初始化TTS
        initializeTTS()
    }

    /**
     * 初始化TTS
     */
    private fun initializeTTS() {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.CHINA)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "中文语言包缺失或不支持")
                }
            } else {
                Log.e("TTS", "TTS初始化失败")
            }
        }
    }

    /**
     * 创建通知渠道
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                importance
            ).apply {
                description = CHANNEL_DESCRIPTION
                setSound(null, null) // 不使用系统声音，避免与语音提醒冲突
            }

            // 注册通知渠道
            val notificationManager: NotificationManager = 
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * 显示服药通知
     * @param reminderId 提醒ID
     * @param title 通知标题
     * @param content 通知内容
     * @param medicineNames 药物名称列表
     * @param isVoiceReminderEnabled 是否启用语音提醒
     */
    fun showReminderNotification(
        reminderId: Long,
        title: String,
        content: String,
        medicineNames: List<String>,
        isVoiceReminderEnabled: Boolean = true
    ) {
        // 创建点击通知后的意图
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("reminderId", reminderId)
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            reminderId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 构建通知内容
        val medicineText = if (medicineNames.isEmpty()) {
            ""
        } else {
            "\n药物：${medicineNames.joinToString("、")}"
        }

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(content + medicineText)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(content + medicineText))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setOngoing(false)

        // 显示通知
        with(NotificationManagerCompat.from(context)) {
            notify((NOTIFICATION_ID_PREFIX + reminderId).toInt(), notificationBuilder.build())
        }

        // 播放语音提醒（如果启用）
        if (isVoiceReminderEnabled) {
            val voiceContent = "$content ${medicineNames.joinToString("、")}"
            speak(voiceContent)
        }
    }

    /**
     * 播放语音
     * @param text 要播放的文本
     */
    private fun speak(text: String) {
        if (tts != null && tts?.isSpeaking == false) {
            tts?.speak(text, TextToSpeech.QUEUE_ADD, null, "utterance_${System.currentTimeMillis()}")
        }
    }

    /**
     * 取消通知
     * @param reminderId 提醒ID
     */
    fun cancelNotification(reminderId: Long) {
        with(NotificationManagerCompat.from(context)) {
            cancel((NOTIFICATION_ID_PREFIX + reminderId).toInt())
        }
    }

    /**
     * 取消所有通知
     */
    fun cancelAllNotifications() {
        with(NotificationManagerCompat.from(context)) {
            cancelAll()
        }
    }

    /**
     * 关闭TTS
     */
    fun shutdown() {
        tts?.shutdown()
    }
}
