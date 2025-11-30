package com.medicinereminder.domain.model

import java.util.Date

/**
 * 服药提醒领域模型
 * 用于UI层展示和交互
 */
data class ReminderModel(
    val id: Long = 0,
    val medicineIds: List<Long>,
    val scheduleType: ScheduleType,
    val timesPerDay: Int = 1,
    val intervalHours: Int = 0,
    val intervalDays: Int = 1,
    val startTime: Date,
    val isActive: Boolean = true,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

/**
 * 提醒类型枚举
 */
enum class ScheduleType {
    /**
     * 每天多次
     */
    DAILY,
    /**
     * 间隔天数
     */
    INTERVAL_DAYS
}
