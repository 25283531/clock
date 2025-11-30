package com.medicinereminder.domain.usecase.reminder

import com.medicinereminder.data.mapper.ReminderMapper
import com.medicinereminder.data.repository.ReminderRepository
import com.medicinereminder.domain.model.ReminderModel

/**
 * 更新服药提醒用例
 * 用于更新数据库中的服药提醒信息
 */
class UpdateReminderUseCase(private val reminderRepository: ReminderRepository) {

    /**
     * 执行更新服药提醒操作
     * @param reminder 要更新的提醒模型
     */
    suspend operator fun invoke(reminder: ReminderModel) {
        val entity = ReminderMapper.toEntity(reminder)
        reminderRepository.updateReminder(entity)
    }
}
