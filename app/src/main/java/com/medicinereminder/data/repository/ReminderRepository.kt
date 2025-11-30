package com.medicinereminder.data.repository

import com.medicinereminder.data.dao.ReminderDao
import com.medicinereminder.data.entity.Reminder
import kotlinx.coroutines.flow.Flow

/**
 * 服药提醒数据仓库
 * 封装服药提醒相关的数据库操作，提供统一的数据访问接口
 */
class ReminderRepository(private val reminderDao: ReminderDao) {

    /**
     * 获取所有提醒，按开始时间排序
     */
    fun getAllReminders(): Flow<List<Reminder>> {
        return reminderDao.getAllReminders()
    }

    /**
     * 获取所有激活的提醒，按开始时间排序
     */
    fun getActiveReminders(): Flow<List<Reminder>> {
        return reminderDao.getActiveReminders()
    }

    /**
     * 根据ID获取提醒
     */
    suspend fun getReminderById(id: Long): Reminder? {
        return reminderDao.getReminderById(id)
    }

    /**
     * 插入提醒
     */
    suspend fun insertReminder(reminder: Reminder): Long {
        return reminderDao.insertReminder(reminder)
    }

    /**
     * 更新提醒
     */
    suspend fun updateReminder(reminder: Reminder) {
        reminderDao.updateReminder(reminder)
    }

    /**
     * 删除提醒
     */
    suspend fun deleteReminder(reminder: Reminder) {
        reminderDao.deleteReminder(reminder)
    }

    /**
     * 根据ID删除提醒
     */
    suspend fun deleteReminderById(id: Long) {
        reminderDao.deleteReminderById(id)
    }

    /**
     * 获取提醒总数
     */
    suspend fun getReminderCount(): Int {
        return reminderDao.getReminderCount()
    }

    /**
     * 获取激活的提醒总数
     */
    suspend fun getActiveReminderCount(): Int {
        return reminderDao.getActiveReminderCount()
    }
}
