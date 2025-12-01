package com.medicinereminder.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.medicinereminder.data.database.DateTypeConverters
import java.util.Date

/**
 * 服药提醒实体类
 * @property id 提醒ID
 * @property medicineIds 关联的药物ID列表，以逗号分隔
 * @property scheduleType 提醒类型：1-每天多次，2-间隔天数
 * @property timesPerDay 每天服药次数（仅用于每天多次类型）
 * @property intervalHours 每次服药间隔小时数（仅用于每天多次类型）
 * @property intervalDays 服药间隔天数（仅用于间隔天数类型）
 * @property startTime 开始服药时间
 * @property isActive 是否激活提醒
 * @property createdAt 创建时间
 * @property updatedAt 更新时间
 */
@Entity(tableName = "reminders")
@TypeConverters(DateTypeConverters::class)
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val medicineIds: String, // 格式："1,2,3"
    val scheduleType: Int, // 1: 每天多次, 2: 间隔天数
    val timesPerDay: Int = 1,
    val intervalHours: Int = 0,
    val intervalDays: Int = 1,
    val startTime: Date,
    val isActive: Boolean = true,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)
