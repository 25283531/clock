package com.medicinereminder.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
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
 * 用于显示待服药提醒列表，支持直接在桌面标记已服药
 */
class MedicineWidget : AppWidgetProvider() {

    companion object {
        // 动作常量
        const val ACTION_MEDICINE_TAKEN = "com.medicinereminder.ACTION_MEDICINE_TAKEN"
        const val EXTRA_REMINDER_ID = "extra_reminder_id"
        const val EXTRA_ROW_INDEX = "extra_row_index"

        /**
         * 待服药提醒数据类
         */
        data class PendingReminder(
            val reminderId: Long,
            val time: Date,
            val medicineNames: List<String>
        )

        /**
         * 更新小组件
         * @param context 上下文
         * @param appWidgetManager 小组件管理器
         * @param appWidgetIds 小组件ID数组
         * @param pendingReminders 待服药提醒列表
         */
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetIds: IntArray,
            pendingReminders: List<PendingReminder>
        ) {
            // 更新每个小组件
            for (appWidgetId in appWidgetIds) {
                updateAppWidget(
                    context,
                    appWidgetManager,
                    appWidgetId,
                    pendingReminders
                )
            }
        }

        /**
         * 更新单个小组件
         * @param context 上下文
         * @param appWidgetManager 小组件管理器
         * @param appWidgetId 小组件ID
         * @param pendingReminders 待服药提醒列表
         */
        private fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            pendingReminders: List<PendingReminder>
        ) {
            // 构建RemoteViews
            val views = RemoteViews(context.packageName, R.layout.medicine_widget_layout_new)

            // 格式化时间
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

            // 根据提醒数量显示或隐藏相应的行
            when (pendingReminders.size) {
                0 -> {
                    // 显示空状态
                    views.setViewVisibility(R.id.reminder_row_1, android.view.View.GONE)
                    views.setViewVisibility(R.id.reminder_row_2, android.view.View.GONE)
                    views.setViewVisibility(R.id.reminder_row_3, android.view.View.GONE)
                    views.setViewVisibility(R.id.reminder_row_4, android.view.View.GONE)
                    views.setViewVisibility(R.id.widget_empty, android.view.View.VISIBLE)
                }
                else -> {
                    // 隐藏空状态
                    views.setViewVisibility(R.id.widget_empty, android.view.View.GONE)

                    // 显示最多4行提醒
                    for (i in 0 until 4) {
                        if (i < pendingReminders.size) {
                            // 获取提醒数据
                            val reminder = pendingReminders[i]
                            val formattedTime = timeFormat.format(reminder.time)
                            val medicineText = reminder.medicineNames.joinToString("、")

                            // 设置行可见性
                            val rowId = when (i) {
                                0 -> R.id.reminder_row_1
                                1 -> R.id.reminder_row_2
                                2 -> R.id.reminder_row_3
                                3 -> R.id.reminder_row_4
                                else -> R.id.reminder_row_1
                            }
                            views.setViewVisibility(rowId, android.view.View.VISIBLE)

                            // 设置时间和药物信息
                            val timeId = when (i) {
                                0 -> R.id.time_1
                                1 -> R.id.time_2
                                2 -> R.id.time_3
                                3 -> R.id.time_4
                                else -> R.id.time_1
                            }
                            val medicineId = when (i) {
                                0 -> R.id.medicine_1
                                1 -> R.id.medicine_2
                                2 -> R.id.medicine_3
                                3 -> R.id.medicine_4
                                else -> R.id.medicine_1
                            }
                            views.setTextViewText(timeId, formattedTime)
                            views.setTextViewText(medicineId, medicineText)

                            // 设置复选框点击事件
                            val checkboxId = when (i) {
                                0 -> R.id.checkbox_1
                                1 -> R.id.checkbox_2
                                2 -> R.id.checkbox_3
                                3 -> R.id.checkbox_4
                                else -> R.id.checkbox_1
                            }
                            val intent = Intent(context, MedicineTakenReceiver::class.java)
                            intent.action = ACTION_MEDICINE_TAKEN
                            intent.putExtra(EXTRA_REMINDER_ID, reminder.reminderId)
                            intent.putExtra(EXTRA_ROW_INDEX, i)
                            val pendingIntent = PendingIntent.getBroadcast(
                                context,
                                (appWidgetId * 10 + i),
                                intent,
                                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                            )
                            views.setOnClickPendingIntent(checkboxId, pendingIntent)
                        } else {
                            // 隐藏超出数量的行
                            val rowId = when (i) {
                                0 -> R.id.reminder_row_1
                                1 -> R.id.reminder_row_2
                                2 -> R.id.reminder_row_3
                                3 -> R.id.reminder_row_4
                                else -> R.id.reminder_row_1
                            }
                            views.setViewVisibility(rowId, android.view.View.GONE)
                        }
                    }
                }
            }

            // 更新小组件
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // 这里应该从数据库获取待服药提醒列表，然后更新小组件
        // 暂时使用模拟数据
        val pendingReminders = generateMockPendingReminders()

        updateAppWidget(
            context,
            appWidgetManager,
            appWidgetIds,
            pendingReminders
        )
    }

    /**
     * 生成模拟的待服药提醒列表
     */
    private fun generateMockPendingReminders(): List<PendingReminder> {
        val currentTime = System.currentTimeMillis()
        return listOf(
            PendingReminder(
                reminderId = 1L,
                time = Date(currentTime + 3600000), // 1小时后
                medicineNames = listOf("阿司匹林", "布洛芬")
            ),
            PendingReminder(
                reminderId = 2L,
                time = Date(currentTime + 7200000), // 2小时后
                medicineNames = listOf("维生素C")
            ),
            PendingReminder(
                reminderId = 3L,
                time = Date(currentTime + 10800000), // 3小时后
                medicineNames = listOf("钙片")
            ),
            PendingReminder(
                reminderId = 4L,
                time = Date(currentTime + 14400000), // 4小时后
                medicineNames = listOf("降压药")
            )
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
            // 重新获取待服药提醒列表并更新小组件
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(context, MedicineWidget::class.java)
            )
            onUpdate(context, appWidgetManager, appWidgetIds)
        }
    }
}
