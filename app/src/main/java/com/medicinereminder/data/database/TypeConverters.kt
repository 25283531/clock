package com.medicinereminder.data.database

import androidx.room.TypeConverter
import java.util.Date

/**
 * Room数据库类型转换器
 * 用于处理Date类型的存储和读取
 */
class DateTypeConverters {

    /**
     * 将Date转换为Long
     */
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    /**
     * 将Long转换为Date
     */
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }
}