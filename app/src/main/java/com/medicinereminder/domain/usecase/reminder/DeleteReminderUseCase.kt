package com.medicinereminder.domain.usecase.reminder

import com.medicinereminder.data.mapper.ReminderMapper
import com.medicinereminder.data.repository.ReminderRepository
import com.medicinereminder.domain.model.ReminderModel

/**
 * 删除服药提醒用例
 * 用于从数据库删除服药提醒
 */
class DeleteReminderUseCase(private val reminderRepository: ReminderRepository) {

    /**
     * 执行删除服药提醒操作
     * @param reminder 要删除的提醒模型
     */
    suspend operator fun invoke(reminder: ReminderModel) {
        val entity = ReminderMapper.toEntity(reminder)
        reminderRepository.deleteReminder(entity)
    }

    /**
     * 执行根据ID删除服药提醒操作
     * @param id 要删除的提醒ID
     */
    suspend operator fun invoke(id: Long) {
        reminderRepository.deleteReminderById(id)
    }
}
