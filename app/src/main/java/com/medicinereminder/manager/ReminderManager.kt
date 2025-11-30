package com.medicinereminder.manager

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.medicinereminder.domain.model.ReminderModel
import com.medicinereminder.domain.model.ScheduleType
import com.medicinereminder.worker.ReminderWorker
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * 提醒管理器
 * 用于处理使用WorkManager设置和取消闹钟的逻辑
 */
class ReminderManager(private val context: Context) {

    /**
     * 设置提醒
     * @param reminder 提醒信息
     */
    fun setReminder(reminder: ReminderModel) {
        // 只处理激活状态的提醒
        if (!reminder.isActive) {
            return
        }

        // 计算下一次提醒时间
        val nextReminderTime = calculateNextReminderTime(reminder)
        if (nextReminderTime == null) {
            return
        }

        // 计算延迟时间
        val delayMillis = nextReminderTime.time - System.currentTimeMillis()
        if (delayMillis <= 0) {
            return
        }

        // 创建工作请求
        val workRequest = OneTimeWorkRequest.Builder(ReminderWorker::class.java)
            .setInputData(Data.Builder()
                .putLong(ReminderWorker.PARAM_REMINDER_ID, reminder.id)
                .build())
            .setConstraints(Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(false)
                .setRequiresBatteryNotLow(false)
                .build())
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .build()

        // 提交工作请求
        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                "reminder_${reminder.id}",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
    }

    /**
     * 取消提醒
     * @param reminderId 提醒ID
     */
    fun cancelReminder(reminderId: Long) {
        WorkManager.getInstance(context)
            .cancelUniqueWork("reminder_${reminderId}")
    }

    /**
     * 取消所有提醒
     */
    fun cancelAllReminders() {
        WorkManager.getInstance(context)
            .cancelAllWorkByTag(ReminderWorker.WORK_NAME)
    }

    /**
     * 重新设置所有提醒
     * @param reminders 提醒列表
     */
    fun resetAllReminders(reminders: List<ReminderModel>) {
        // 取消所有现有提醒
        cancelAllReminders()
        
        // 重新设置所有激活的提醒
        reminders.filter { it.isActive }
            .forEach { setReminder(it) }
    }

    /**
     * 计算下一次提醒时间
     * @param reminder 提醒信息
     * @return 下一次提醒时间，如果无法计算则返回null
     */
    private fun calculateNextReminderTime(reminder: ReminderModel): Date? {
        val now = Calendar.getInstance()
        val reminderTime = Calendar.getInstance().apply {
            time = reminder.startTime
        }

        return when (reminder.scheduleType) {
            ScheduleType.DAILY -> {
                // 每天多次提醒
                calculateDailyReminderTime(reminder, now, reminderTime)
            }
            ScheduleType.INTERVAL_DAYS -> {
                // 间隔天数提醒
                calculateIntervalDaysReminderTime(reminder, now, reminderTime)
            }
        }
    }

    /**
     * 计算每天多次提醒的下一次时间
     */
    private fun calculateDailyReminderTime(
        reminder: ReminderModel,
        now: Calendar,
        reminderTime: Calendar
    ): Date {
        // 设置提醒时间的日期为今天
        reminderTime.set(Calendar.YEAR, now.get(Calendar.YEAR))
        reminderTime.set(Calendar.MONTH, now.get(Calendar.MONTH))
        reminderTime.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH))

        // 如果今天的提醒时间已过，计算下一次提醒时间
        if (reminderTime.before(now)) {
            // 计算今天的所有提醒时间点
            val todayReminderTimes = generateTodayReminderTimes(reminder, now)
            // 找到下一个提醒时间点
            for (time in todayReminderTimes) {
                if (time.after(now)) {
                    return time.time
                }
            }
            // 如果今天的所有提醒时间都已过，设置为明天的第一个提醒时间
            reminderTime.add(Calendar.DAY_OF_MONTH, 1)
        }

        return reminderTime.time
    }

    /**
     * 计算间隔天数提醒的下一次时间
     */
    private fun calculateIntervalDaysReminderTime(
        reminder: ReminderModel,
        now: Calendar,
        reminderTime: Calendar
    ): Date? {
        // 如果提醒时间已过，计算下一次提醒时间
        if (reminderTime.before(now)) {
            // 计算距离上次提醒的天数
            val diffInMillis = now.timeInMillis - reminderTime.timeInMillis
            val diffInDays = diffInMillis / (1000 * 60 * 60 * 24)
            // 计算需要添加的天数
            val daysToAdd = (diffInDays / reminder.intervalDays + 1) * reminder.intervalDays
            // 设置下一次提醒时间
            reminderTime.add(Calendar.DAY_OF_MONTH, daysToAdd.toInt())
        }

        return reminderTime.time
    }

    /**
     * 生成今天的所有提醒时间点
     */
    private fun generateTodayReminderTimes(
        reminder: ReminderModel,
        now: Calendar
    ): List<Calendar> {
        val times = mutableListOf<Calendar>()
        val firstReminderTime = Calendar.getInstance().apply {
            time = reminder.startTime
            set(Calendar.YEAR, now.get(Calendar.YEAR))
            set(Calendar.MONTH, now.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH))
        }

        // 添加第一次提醒时间
        times.add(firstReminderTime.clone() as Calendar)

        // 添加后续的提醒时间
        for (i in 1 until reminder.timesPerDay) {
            val nextTime = firstReminderTime.clone() as Calendar
            nextTime.add(Calendar.HOUR_OF_DAY, reminder.intervalHours * i)
            times.add(nextTime)
        }

        return times
    }
}
