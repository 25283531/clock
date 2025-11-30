package com.medicinereminder.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.medicinereminder.data.entity.Reminder
import kotlinx.coroutines.flow.Flow

/**
 * 服药提醒数据访问对象
 */
@Dao
interface ReminderDao {
    /**
     * 获取所有提醒，按开始时间排序
     */
    @Query("SELECT * FROM reminders ORDER BY startTime ASC")
    fun getAllReminders(): Flow<List<Reminder>>

    /**
     * 获取所有激活的提醒，按开始时间排序
     */
    @Query("SELECT * FROM reminders WHERE isActive = 1 ORDER BY startTime ASC")
    fun getActiveReminders(): Flow<List<Reminder>>

    /**
     * 根据ID获取提醒
     */
    @Query("SELECT * FROM reminders WHERE id = :id")
    suspend fun getReminderById(id: Long): Reminder?

    /**
     * 插入提醒
     */
    @Insert
    suspend fun insertReminder(reminder: Reminder): Long

    /**
     * 更新提醒
     */
    @Update
    suspend fun updateReminder(reminder: Reminder)

    /**
     * 删除提醒
     */
    @Delete
    suspend fun deleteReminder(reminder: Reminder)

    /**
     * 根据ID删除提醒
     */
    @Query("DELETE FROM reminders WHERE id = :id")
    suspend fun deleteReminderById(id: Long)

    /**
     * 获取提醒总数
     */
    @Query("SELECT COUNT(*) FROM reminders")
    suspend fun getReminderCount(): Int

    /**
     * 获取激活的提醒总数
     */
    @Query("SELECT COUNT(*) FROM reminders WHERE isActive = 1")
    suspend fun getActiveReminderCount(): Int
}
