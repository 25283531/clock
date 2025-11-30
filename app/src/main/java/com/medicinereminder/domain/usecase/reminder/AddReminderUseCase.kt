package com.medicinereminder.domain.usecase.reminder

import com.medicinereminder.data.mapper.ReminderMapper
import com.medicinereminder.data.repository.ReminderRepository
import com.medicinereminder.domain.model.ReminderModel

/**
 * 添加服药提醒用例
 * 用于添加新的服药提醒到数据库
 */
class AddReminderUseCase(private val reminderRepository: ReminderRepository) {

    /**
     * 执行添加服药提醒操作
     * @param reminder 要添加的提醒模型
     * @return 添加成功后的提醒ID
     */
    suspend operator fun invoke(reminder: ReminderModel): Long {
        val entity = ReminderMapper.toEntity(reminder)
        return reminderRepository.insertReminder(entity)
    }
}
