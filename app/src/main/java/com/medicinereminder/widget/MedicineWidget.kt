package com.medicinereminder.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.medicinereminder.R
import com.medicinereminder.utils.MedicineTakenReceiver
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 药物提醒桌面小组件
 * 用于显示下次服药时间和药物信息，支持直接在桌面标记已服药
 */
class MedicineWidget : AppWidgetProvider() {

    companion object {
        // 动作常量
        const val ACTION_MEDICINE_TAKEN = "com.medicinereminder.ACTION_MEDICINE_TAKEN"
        const val EXTRA_REMINDER_ID = "extra_reminder_id"

        /**
         * 更新小组件
         * @param context 上下文
         * @param appWidgetManager 小组件管理器
         * @param appWidgetIds 小组件ID数组
         * @param nextDoseTime 下次服药时间
         * @param medicines 下次服药的药物列表
         * @param reminderId 关联的提醒ID
         */
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetIds: IntArray,
            nextDoseTime: Date,
            medicines: List<String>,
            reminderId: Long
        ) {
            // 格式化时间
            val timeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val formattedTime = timeFormat.format(nextDoseTime)

            // 格式化药物列表
            val medicinesText = if (medicines.isEmpty()) {
                context.getString(R.string.no_medicines)
            } else {
                medicines.joinToString(", ")
            }

            // 更新每个小组件
            for (appWidgetId in appWidgetIds) {
                updateAppWidget(
                    context,
                    appWidgetManager,
                    appWidgetId,
                    formattedTime,
                    medicinesText,
                    reminderId
                )
            }
        }

        /**
         * 更新单个小组件
         * @param context 上下文
         * @param appWidgetManager 小组件管理器
         * @param appWidgetId 小组件ID
         * @param nextDoseTime 格式化后的下次服药时间
         * @param medicines 格式化后的药物列表
         * @param reminderId 关联的提醒ID
         */
        private fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            nextDoseTime: String,
            medicines: String,
            reminderId: Long
        ) {
            // 构建RemoteViews
            val views = RemoteViews(context.packageName, R.layout.medicine_widget_layout)

            // 设置文本
            views.setTextViewText(R.id.widget_time, nextDoseTime)
            views.setTextViewText(R.id.widget_medicines, medicines)

            // 设置服药按钮点击事件
            val intent = Intent(context, MedicineTakenReceiver::class.java)
            intent.action = ACTION_MEDICINE_TAKEN
            intent.putExtra(EXTRA_REMINDER_ID, reminderId)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                appWidgetId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_take_medicine, pendingIntent)

            // 更新小组件
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // 这里应该从数据库获取下次服药信息，然后更新小组件
        // 暂时使用模拟数据
        val mockNextDoseTime = Date(System.currentTimeMillis() + 3600000) // 1小时后
        val mockMedicines = listOf("阿司匹林", "布洛芬")
        val mockReminderId = 1L

        updateAppWidget(
            context,
            appWidgetManager,
            appWidgetIds,
            mockNextDoseTime,
            mockMedicines,
            mockReminderId
        )
    }

    override fun onEnabled(context: Context) {
        // 小组件首次添加时调用
    }

    override fun onDisabled(context: Context) {
        // 最后一个小组件被移除时调用
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        // 处理自定义广播
        if (intent.action == ACTION_MEDICINE_TAKEN) {
            // 重新获取下次服药信息并更新小组件
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(context, MedicineWidget::class.java)
            )
            onUpdate(context, appWidgetManager, appWidgetIds)
        }
    }
}
